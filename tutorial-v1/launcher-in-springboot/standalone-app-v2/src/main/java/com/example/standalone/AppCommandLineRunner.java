package com.example.standalone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppCommandLineRunner implements CommandLineRunner {

    private Environment environment;

    @Autowired
    public AppCommandLineRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        long pid = ProcessHandle.current().pid();
        System.out.println("Process ID: " + pid);

        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid2 = name.split("@")[0];
        System.out.println("Process ID 2: " + pid2);


        String pid3 = environment.getProperty("PID");
        System.out.println("Process ID from Environment: " + pid3);
    }
}
