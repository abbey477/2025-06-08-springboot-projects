package com.mycompany.app7;

import lombok.Builder;

@Builder(toBuilder = true)
record User(String name, int age, String email) {}