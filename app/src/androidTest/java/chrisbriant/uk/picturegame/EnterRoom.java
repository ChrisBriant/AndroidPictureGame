package chrisbriant.uk.picturegame;

import android.util.Log;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;
import java.util.Random;

import chrisbriant.uk.picturegame.activities.MainActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EnterRoom {


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void enterName() throws InterruptedException {
        //Enter name
        onView(withId(R.id.enterNameTxt)).perform((clearText()))
                .perform(typeText("Engineer"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.sendNameBtn)).perform(click());
        //Add room name
        String generatedString = RandomString.getAlphaNumericString(8);

        Log.d("Random String",generatedString);

        onView(withId(R.id.rmNewRoomEdt)).perform((clearText()))
                .perform(typeText(generatedString));
        onView(withId(R.id.rmNewRoomEdt)).perform((clearText()))
                .perform(typeText(generatedString));
        //Click add room
        onView(withId(R.id.rmAddRoomBtn)).perform(click());
        //Click the top item of the recycler view
        Thread.sleep(2000);
        //onData(withId(R.id.rmRoomListRecyc)).atPosition(0).perform(click());
//        onView(withId(R.id.rmRoomListRecyc))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Leave open for 10 seconds after
        Thread.sleep(60000);
    }

    @AfterClass
    public static void afterClass() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }
}
