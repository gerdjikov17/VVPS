
class LineModelUnitTest {
    private LineModel lineModel;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        lineModel = new LineModel("\"3/11/18, 14:41\",Course: VVPS,Logs,Log report viewed,The user with id '2' viewed the log report for the course with id '49'.,web,212.5.158.162");
    }

    @org.junit.jupiter.api.Test
    void testInit() {
        assert lineModel != null;
        assert lineModel.getIp() != null;
        assert lineModel.getTime() != null;
        assert lineModel.getComponent() != null;
        assert lineModel.getDescription() != null;
        assert lineModel.getEventContext() != null;
        assert lineModel.getEventName() != null;
        assert lineModel.getOrigin() != null;
        assert lineModel.getTrimmedLine() != null;
    }

    @org.junit.jupiter.api.Test
    void getTrimmedLine() {
        assert lineModel.getTrimmedLine().equals(lineModel.getEventName());
    }

    @org.junit.jupiter.api.Test
    void toString1() {
        assert lineModel.toString().equals("LineModel{" +
                "time='" + "\"3/11/18, 14:41\"" + '\'' +
                ", eventContext='" + "Course: VVPS" + '\'' +
                ", component='" + "Logs" + '\'' +
                ", eventName='" + "Log|report|viewed" + '\'' +
                ", description='" + "The user with id '2' viewed the log report for the course with id '49'." + '\'' +
                ", origin='" + "web" + '\'' +
                ", ip='" + "212.5.158.162" + '\'' +
                '}');
    }
}