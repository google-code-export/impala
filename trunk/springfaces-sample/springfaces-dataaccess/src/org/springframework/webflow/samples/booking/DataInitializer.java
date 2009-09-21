package org.springframework.webflow.samples.booking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

public class DataInitializer implements InitializingBean, BeanClassLoaderAware {
    
    private DataSource dataSource;
    private ClassLoader classLoader;

    public void afterPropertiesSet() throws Exception {
        
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int customers = template.queryForInt("select count(*) from Customer ");
        if (customers == 0) {
            String result = readImportText();
            String[] sqlArray = StringUtils.tokenizeToStringArray(result, "\r\n");
            for (String sql : sqlArray) {
                if (StringUtils.hasText(sql)) {
                    System.out.println("Executing SQL: " + sql);
                    template.execute(sql.trim());
                }
            }
        }
    }


    private String readImportText() throws IOException {
        
        ClassPathResource resource = new ClassPathResource("META-INF/import.sql", classLoader);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            StringWriter writer = new StringWriter();
            FileCopyUtils.copy(new InputStreamReader(inputStream), writer);
            return writer.toString();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
