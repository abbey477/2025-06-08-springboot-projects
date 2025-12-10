package com.example.standalone;

import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    public long getCurrentProcessId() {
        return ProcessHandle.current().pid();
    }

    public String getCurrentProcessIdAsString() {
        return String.valueOf(ProcessHandle.current().pid());
    }
}
