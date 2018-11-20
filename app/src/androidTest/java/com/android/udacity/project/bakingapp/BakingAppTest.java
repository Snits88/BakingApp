package com.android.udacity.project.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BakingAppTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;


    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void MainActivityUITest() {
        onView(withId(R.id.recyclerview_recipe_name)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recyclerview_recipe_name)).check(matches(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.recyclerview_recipe_name)).check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recyclerview_recipe_name)).check(matches(hasDescendant(withText("Cheesecake"))));
    }

    @Test
    public void RecipeInfoActivityUITest() {
        onView(withId(R.id.recyclerview_recipe_name)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView((withId(R.id.btn_ingredients))).check(matches(withText("Ingredients")));
        onView(withId(R.id.recyclerview_recipe_step)).check(matches(hasDescendant(withSubstring("Step n° 0"))));
    }

   @Test
    public void FragmentIngredientsUITest() {
        onView(withId(R.id.recyclerview_recipe_name)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView((withId(R.id.btn_ingredients))).perform(click());
        onView(withId(R.id.recyclerview_recipe_ingredients)).check(matches(hasDescendant(withText("Graham Cracker crumbs"))));
    }

    @Test
    public void FragmentStepUITest() {
        onView(withId(R.id.recyclerview_recipe_name)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recyclerview_recipe_step)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_title_detail)).check(matches(withSubstring("Recipe Introduction")));
    }


    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
