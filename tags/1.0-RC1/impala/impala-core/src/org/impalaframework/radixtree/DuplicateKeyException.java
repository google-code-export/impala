package org.impalaframework.radixtree;

/*
The MIT License

Copyright (c) 2008 Tahseen Ur Rehman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

/**
 * Exception thrown if a duplicate key is inserted in a {@link RadixTree}
 * 
 * Taken from the original source from the radixtree project (http://radixtree.googlecode.com/)
 * Licence has been left unchanged as the MIT licence for this source file. 
 * @author Tahseen Ur Rehman 
 * email: tahseen.ur.rehman {at.spam.me.not} gmail.com 
 */
public class DuplicateKeyException extends RuntimeException
{
    private static final long serialVersionUID = 3141795907493885706L;

    public DuplicateKeyException(String msg)
    {
        super(msg);
    }
}
