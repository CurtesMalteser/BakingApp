package com.curtesmalteser.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.curtesmalteser.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by António "Curtes Malteser" Bastião on 15/04/2018.
 */

@RunWith(AndroidJUnit4.class)
public class NavigateFromRecipeActivityToDetailActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mDetailsActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void openDetailActivity() {

        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        String itemElementText = "Nutella Pie";
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

}
