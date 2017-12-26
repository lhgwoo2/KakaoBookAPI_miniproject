package com.boostcamp.assignment.Activity;

import android.content.res.Resources;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;

import com.boostcamp.assignment.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by 현기 on 2017-12-03.
 */

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class AssignActivityTest {

    @Rule
    public ActivityTestRule<AssignActivity> testRule = new ActivityTestRule<AssignActivity>(AssignActivity.class);

    @Mock
    private Resources mockResources;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(mockResources);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadStringResource() throws Exception {
        String appName = testRule.getActivity().getResources().getString(R.string.app_name);
        assertThat(appName, is("Assignment"));
    }

    @Test
    public void loadMockStringResource() throws Exception {
        when(mockResources.getString(R.string.app_name)).thenReturn("Assignment");
        String appName = mockResources.getString(R.string.app_name);
        assertThat(appName, is("Assignment"));
    }

}