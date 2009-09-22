/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.service.filter.ldap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of parser which builds a node tree containing as a root node
 * a {@link FilterNode} object.
 * 
 * @author Phil Zoio
 */
public class FilterParser {
    
    static final String UNESCAPED_STARS = FilterParser.class + ".UNESCAPED_STARS:";

    private Chars filterChars;

    public FilterParser(String filterString) {
        super();
        this.filterChars = new Chars(filterString);
    }
    
    public FilterNode parse() {
        final FilterNode node = parseNode();    
        checkForExtraEndParenthesis();
        checkForBadChars();
        return node;
    }

    private FilterNode parseNode() {

        char first = filterChars.advanceToFirstNonWhitespaceChar();
        checkForMissingStartParenthesis(first);
        filterChars.setExpectingOpen(true);

        try {
        
            char c = filterChars.advanceToFirstNonWhitespaceChar();
            if ('&' == c) {
                return parseAnd();
            } else if ('|' == c) {
                return parseOr();
            } else if ('!' == c) {
                return parseNot();
            } else {
                return parseItem();
            }
            
        } finally {
            endParseNode();
        }

    } 

    private FilterNode parseItem() {
        
        //go back because we are not starting with (
        filterChars.back();

        checkForExtraStartParenthesis();

        String key = filterChars.charsTo('=');

        char previous = filterChars.previous();
        Operator operator = getOperator(previous);

        if (operator != Operator.eq) {
            key = key.substring(0, key.length() - 1);
        }

        filterChars.next();
        List<String> value = parseValue();

        return createNode(operator, key.trim(), value);
    }

    protected Operator getOperator(char operatorIdentifier) {
        Operator operator = Operator.eq;

        switch (operatorIdentifier) {
        case '~':
            operator = Operator.apprx;
            break;
        case '<':
            operator = Operator.lt;
            break;
        case '>':
            operator = Operator.gt;
            break;
        }
        return operator;
    }

    private List<String> parseValue() {

        List<String> strings = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        
        boolean ok = true;
        
        while (ok) {
            
            if (!filterChars.moreChars()) {
                break;
            }
            
            char c = filterChars.current();
        
            switch (c) {
                case ')': {
                    ok = false; 
                    break;
                }
            
                case '(': {
                    throw new IllegalArgumentException("Invalid character ) at position " + filterChars.position + " in '" + filterChars.string + "'"); 
                }
                
                case '*': {
                    strings.add(sb.toString().trim());
                    sb = new StringBuffer();
                    filterChars.next();
                    break;
                }
            
                case '\\' : {
                    //deliberately fall through
                    filterChars.next();
                    c = filterChars.current();
                }
                
                default: {
                    sb.append(c);
                    filterChars.next();
                    break;
                }
            }
        }

        if (strings.isEmpty() && sb.length() == 0) {
            throw new IllegalArgumentException("Missing filter value at position  " + filterChars.position + " in '" + filterChars.string + "'"); 
        }
        
        strings.add(sb.toString().trim());
        
        return strings;
    }

    private NotNode parseNot() {
        FilterNode node = parseNode();      
        checkForBadChars();
        checkForMissingEndParenthesis();
        
        return new NotNode(node);
    }

    private OrNode parseOr() {
        List<FilterNode> list = buildChildList();
        return new OrNode(list);
    }

    private AndNode parseAnd() {
        List<FilterNode> list = buildChildList();
        return new AndNode(list);
    }

    private List<FilterNode> buildChildList() {
        List<FilterNode> list = new LinkedList<FilterNode>();
        filterChars.back();
        while (filterChars.isNextNonWhitespaceChar('(')) {
            list.add(parseNode());
        }
        checkForBadChars();
        return list;
    }

    private void checkForExtraStartParenthesis() {
        assertTrue(filterChars.current() != '(', "Extra left parenthesis at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
    }

    private void checkForMissingStartParenthesis(char first) {
        assertTrue(first == '(', "Missing left parenthesis at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
    }

    private void checkForMissingEndParenthesis() {
        assertTrue(filterChars.isNextNonWhitespaceChar(')'), "Missing end parenthesis at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
    }
    
    private void checkForExtraEndParenthesis() {
        assertTrue(!filterChars.isNextNonWhitespaceChar(')'), "Extra end parenthesis at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
    }

    private void endParseNode() {
        assertTrue(filterChars.advanceUntilFindChar(')'), "Missing end parenthesis at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
        filterChars.setExpectingOpen(true);
    }

    private void checkForBadChars() {
        final char firstNonWhitespaceChar = filterChars.getFirstNonWhitespaceChar();
        
        if (firstNonWhitespaceChar != (char)-1 && firstNonWhitespaceChar != '(' && firstNonWhitespaceChar != ')') {
            filterChars.advanceToFirstNonWhitespaceChar();
            throw new IllegalArgumentException("Invalid char '" + firstNonWhitespaceChar + " at position " + filterChars.position + " in filter string '" + filterChars.string + "'");
        }
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    private FilterNode createNode(Operator operator, String name, List<String> values) {
        
        if (values.size() == 1) {
            return createSingleValueNode(operator, name, values.get(0));
        } else {
            return createMultiValueNode(operator, name, values);
        }
    }

    /**
     * Creates {@link FilterNode} when value contains list of items. Possible for subclasses to 
     * override this and create different/additional node types
     * @param operator the operator definition used
     * @param name the name part of the key
     * @param values a list of supplied values
     * @return a {@link FilterNode} instance.
     */
    protected FilterNode createMultiValueNode(Operator operator, String name, List<String> values) {
        boolean hasNonEmptyValue = false;
        for (String value : values) {
            if (value.trim().length() > 0) {
                hasNonEmptyValue = true;
            }
        }
        if (hasNonEmptyValue)
            return new SubstringNode(name, values);
        else 
            return new PresentNode(name);
    }

    /**
     * Creates {@link FilterNode} when value contains list of items. Possible for subclasses to 
     * override this and create different/additional node types
     * @param operator the operator definition used
     * @param name the name part of the key
     * @param value the single value supplied
     * @return a {@link FilterNode} instance.
     */
    protected FilterNode createSingleValueNode(Operator operator, String name, String value) {
        if (operator == Operator.eq) {
            return new EqualsNode(name, value);
        }
        if (operator == Operator.lt) {
            return new LessThanNode(name, value);
        }
        if (operator == Operator.gt) {
            return new GreaterThanNode(name, value);
        }
        if (operator == Operator.apprx) {
            return new ApproxNode(name, value);
        }
        return null;
    }
}

class Chars {

    int position;
    char[] chars;
    String string;
    boolean expectingOpen;
    
    public Chars(String string) {
        super();
        this.string = string;
        this.chars = string.toCharArray();
        this.position = 0;
    }

    public void setExpectingOpen(boolean b) {
        this.expectingOpen = true;
    }

    /**
     * Returns first non-whitespace character, starting from current position.
     * Advances current position to point where match is made.
     */
    char advanceToFirstNonWhitespaceChar() {
        return getFirstNonWhitespaceChar(true);
    }
    
    /**
     * Returns first non-whitespace character, starting from current position.
     * Does not advance current position.
     */
    char getFirstNonWhitespaceChar() {
        return getFirstNonWhitespaceChar(false);
    }
    
    char getFirstNonWhitespaceChar(boolean advance) {
        final int startPosition = this.position;
        for (int i = startPosition; i < this.chars.length; i++) {
            char c = this.chars[i];
            if (!Character.isWhitespace(c)) {
                if (advance) this.position++;
                return c;
            }
            if (advance)
                this.position++;
        }
        return (char) -1;
    }
    
    /**
     * Looks for sought char until it reaches end of String. Returns true if
     * found. Search starts from current position. Also advances the
     * current position
     */
    boolean advanceUntilFindChar(char sought) {
        final int startPosition = this.position;
        for (int i = startPosition; i < this.chars.length; i++) {
            char c = this.chars[i];
            if (c == sought) {
                return true;
            }
            this.position++;
        }
        return false;
    }
    
    /**
     * Checks is next non whitespace character is sought character.
     * Search starts from next position
     */
    boolean isNextNonWhitespaceChar(char sought) {
        next();
        if (!moreChars()) {
            return false;
        }
        
        final int startPosition = this.position;
        for (int i = startPosition; i < this.chars.length; i++) {
            char c = this.chars[i];
            if (!Character.isWhitespace(c)) {
                return (c == sought);
            }
            this.position++;
        }
        return false;
    }

    /**
     * Returns current character, or (char)-1 if position out of bounds
     */
    public char current() {
        if (this.position < this.chars.length) {
            final char c = this.chars[this.position];
            return c;
        }
        return (char)-1;
    }

    /**
     * Returns the next set of characters to the specified character, returned as a String
     * Advances position to where it finds specified char.
     */
    String charsTo(char toChar) {
        final int startPosition = this.position;
        for (int i = startPosition; i < this.chars.length; i++) {
            char c = this.chars[i];
            if (c == toChar) {
                break;
            }
            this.position++;
        }
        char[] newChars = new char[this.position - startPosition];
        System.arraycopy(this.chars, startPosition, newChars, 0,
                newChars.length);
        return new String(newChars);
    }

    /**
     * Returns the character for the previous position
     */
    char previous() {
        return this.chars[this.position - 1];
    }

    /**
     * Moves the current position one back
     */
    void back() {
        this.position--;
    }

    /**
     * Moves the current position one forward
     */
    void next() {
        this.position++;
    }

    /**
     * Returns true if there are more characters from the current position
     */
    boolean moreChars() {
        return this.position < this.chars.length;
    }

    @Override
    public String toString() {
        return "Postion: " + position + ", current: " + current() + ", full string: " + this.string;
    }

}
