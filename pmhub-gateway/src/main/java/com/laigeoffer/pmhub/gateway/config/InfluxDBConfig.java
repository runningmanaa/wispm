package com.laigeoffer.pmhub.gateway.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Bean
    public InfluxDB influxDB() {
        String url = "http://47.122.66.174:8086"; // InfluxDB服务器地址
        String username = "admin"; // InfluxDB用户名
        String password = "123456"; // InfluxDB密码
        String dbName = "duration_statistics"; // 数据库名

        InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);
        influxDB.setDatabase(dbName);

        return influxDB;
    }
}