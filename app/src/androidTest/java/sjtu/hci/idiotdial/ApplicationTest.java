package sjtu.hci.idiotdial;

import android.app.Application;
import android.test.ApplicationTestCase;

import sjtu.hci.idiotdial.manager.AudioManager;
import sjtu.hci.idiotdial.manager.RecognizeManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        RecognizeManager.main(null);
    }
}