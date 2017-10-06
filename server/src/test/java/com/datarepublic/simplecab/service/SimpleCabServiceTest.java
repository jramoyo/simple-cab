package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.repository.SimpleCabRepository;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCabServiceTest {

    @Mock
    private SimpleCabRepository repository;

    @InjectMocks
    private SimpleCabService service;

    @Test
    public void it_fetches_from_the_repository_then_fetches_from_the_cache_afterwards() {
        String medallion1 = "medallion1";
        String medallion2 = "medallion2";
        String medallion3 = "medallion3";
        Date pickupDate = new Date();

        when(repository.getCountByMedallionAndPickupDatetime(medallion1, pickupDate)).thenReturn(1);
        when(repository.getCountByMedallionAndPickupDatetime(medallion2, pickupDate)).thenReturn(2);
        when(repository.getCountByMedallionAndPickupDatetime(medallion3, pickupDate)).thenReturn(3);

        MedallionsSummary summary = service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);

        assertThat(summary.getTrips().get("medallion1"), is(1));
        assertThat(summary.getTrips().get("medallion2"), is(2));
        assertThat(summary.getTrips().get("medallion3"), is(3));

        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);
        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);
        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);

        verify(repository, times(1)).getCountByMedallionAndPickupDatetime(medallion1, pickupDate);
        verify(repository, times(1)).getCountByMedallionAndPickupDatetime(medallion2, pickupDate);
        verify(repository, times(1)).getCountByMedallionAndPickupDatetime(medallion3, pickupDate);
    }

    @Test
    public void it_always_fetches_from_the_repository_if_ignoreCache_is_true() {
        String medallion1 = "medallion1";
        String medallion2 = "medallion2";
        String medallion3 = "medallion3";
        Date pickupDate = new Date();

        when(repository.getCountByMedallionAndPickupDatetime(medallion1, pickupDate)).thenReturn(1);
        when(repository.getCountByMedallionAndPickupDatetime(medallion2, pickupDate)).thenReturn(2);
        when(repository.getCountByMedallionAndPickupDatetime(medallion3, pickupDate)).thenReturn(3);

        MedallionsSummary summary = service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);

        assertThat(summary.getTrips().get("medallion1"), is(1));
        assertThat(summary.getTrips().get("medallion2"), is(2));
        assertThat(summary.getTrips().get("medallion3"), is(3));

        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate, true);
        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate, true);
        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate, true);

        verify(repository, times(4)).getCountByMedallionAndPickupDatetime(medallion1, pickupDate);
        verify(repository, times(4)).getCountByMedallionAndPickupDatetime(medallion2, pickupDate);
        verify(repository, times(4)).getCountByMedallionAndPickupDatetime(medallion3, pickupDate);
    }

    @Test
    public void it_allows_the_cache_to_be_invalidated() {
        String medallion1 = "medallion1";
        String medallion2 = "medallion2";
        String medallion3 = "medallion3";
        Date pickupDate = new Date();

        when(repository.getCountByMedallionAndPickupDatetime(medallion1, pickupDate)).thenReturn(1);
        when(repository.getCountByMedallionAndPickupDatetime(medallion2, pickupDate)).thenReturn(2);
        when(repository.getCountByMedallionAndPickupDatetime(medallion3, pickupDate)).thenReturn(3);

        MedallionsSummary summary = service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);

        assertThat(summary.getTrips().get("medallion1"), is(1));
        assertThat(summary.getTrips().get("medallion2"), is(2));
        assertThat(summary.getTrips().get("medallion3"), is(3));

        service.invalidateCache();

        service.getMedallionsSummary(Lists.newArrayList(medallion1, medallion2, medallion3), pickupDate);

        verify(repository, times(2)).getCountByMedallionAndPickupDatetime(medallion1, pickupDate);
        verify(repository, times(2)).getCountByMedallionAndPickupDatetime(medallion2, pickupDate);
        verify(repository, times(2)).getCountByMedallionAndPickupDatetime(medallion3, pickupDate);
    }
}
