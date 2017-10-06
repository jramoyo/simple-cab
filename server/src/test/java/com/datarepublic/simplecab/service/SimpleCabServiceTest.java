package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.repository.SimpleCabRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCabServiceTest {

    @Mock
    private SimpleCabRepository repository;

    @InjectMocks
    private SimpleCabService service;

    @Test
    public void it_does_something() {
    }
}
