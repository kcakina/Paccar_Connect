package mobile.paccar.com.paccar;

/**
 * Created by shiyizhang on 6/1/16.
 */
public enum SeverityLevel {

    NotSet(0),
    High(1),
    Medium(2),
    Low(3);

    private int SeverityLevelIndex;

    private SeverityLevel(int SeverityIndex) { this.SeverityLevelIndex = SeverityIndex; }

}
