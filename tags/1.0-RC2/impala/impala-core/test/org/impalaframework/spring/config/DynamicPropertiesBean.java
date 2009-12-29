package org.impalaframework.spring.config;

import java.util.Date;

import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.DatePropertyValue;
import org.impalaframework.config.DoublePropertyValue;
import org.impalaframework.config.FloatPropertyValue;
import org.impalaframework.config.IntPropertyValue;
import org.impalaframework.config.LongPropertyValue;
import org.impalaframework.config.StringPropertyValue;

public class DynamicPropertiesBean {

    private BooleanPropertyValue booleanPropertyValue;
    
    private DatePropertyValue datePropertyValue;
    
    private DoublePropertyValue doublePropertyValue;
    
    private FloatPropertyValue floatPropertyValue;
    
    private IntPropertyValue intPropertyValue;
    
    private LongPropertyValue longPropertyValue;
    
    private StringPropertyValue stringPropertyValue;
    
    public void print() {
        System.out.println("boolean: " + getBooleanValue());
        System.out.println("date: " + getDateValue());
        System.out.println("double: " + getDoubleValue());
        System.out.println("float: " + getFloatValue());
        System.out.println("int: " + getIntValue());
        System.out.println("long: " + getLongValue());
        System.out.println("string: " + getStringValue());
    }

    public boolean getBooleanValue() {
        return booleanPropertyValue.getValue();
    }

    public void setBooleanPropertyValue(BooleanPropertyValue booleanPropertyValue) {
        this.booleanPropertyValue = booleanPropertyValue;
    }

    public Date getDateValue() {
        return datePropertyValue.getValue();
    }

    public void setDatePropertyValue(DatePropertyValue datePropertyValue) {
        this.datePropertyValue = datePropertyValue;
    }

    public double getDoubleValue() {
        return doublePropertyValue.getValue();
    }

    public void setDoublePropertyValue(DoublePropertyValue doublePropertyValue) {
        this.doublePropertyValue = doublePropertyValue;
    }

    public float getFloatValue() {
        return floatPropertyValue.getValue();
    }

    public void setFloatPropertyValue(FloatPropertyValue floatPropertyValue) {
        this.floatPropertyValue = floatPropertyValue;
    }

    public int getIntValue() {
        return intPropertyValue.getValue();
    }

    public void setIntPropertyValue(IntPropertyValue intPropertyValue) {
        this.intPropertyValue = intPropertyValue;
    }

    public long getLongValue() {
        return longPropertyValue.getValue();
    }

    public void setLongPropertyValue(LongPropertyValue longPropertyValue) {
        this.longPropertyValue = longPropertyValue;
    }

    public String getStringValue() {
        return stringPropertyValue.getValue();
    }

    public void setStringPropertyValue(StringPropertyValue stringPropertyValue) {
        this.stringPropertyValue = stringPropertyValue;
    }
    
    
    
}
