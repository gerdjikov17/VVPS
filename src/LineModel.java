public class LineModel {
    //Time,Event context,Component,Event name,Description,Origin,IP address
    //"3/11/18, 14:41",Course: ��������� �����,Logs,Log report viewed,The user with id '2' viewed the log report for the course with id '49'.,web,212.5.158.162

    String time;
    private String eventContext;
    private String component;
    private String eventName;
    private String description;
    private String origin;
    String ip;

    private String line;

    public LineModel(String line) {
        line = line.replaceAll("�+.*�", "");
        this.line = line;
        String trimmedLine;
        int lastIndex = line.lastIndexOf("\"");
        time = line.substring(0, lastIndex + 1);
        trimmedLine = line.substring(lastIndex + 2);
        int firstIndex = line.indexOf(',');

        eventContext = trimmedLine.substring(0, firstIndex + 1);
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');

        component = trimmedLine.substring(0, firstIndex + 1);
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');

        eventName = trimmedLine.substring(0, firstIndex + 1);
        eventName = eventName.replace(",", "");
        eventName = eventName.replace(" ", "|");
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');

        description = trimmedLine.substring(0, firstIndex + 1);
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');

        origin = trimmedLine.substring(0, firstIndex + 1);
        trimmedLine = trimmedLine.substring(firstIndex + 1);

        ip = trimmedLine;
        ip = ip.replace(",", "");
    }

    public String getTrimmedLine() {
        return this.eventName;
    }

    @Override
    public String toString() {
        return "LineModel{" +
                "time='" + time + '\'' +
                ", eventContext='" + eventContext + '\'' +
                ", component='" + component + '\'' +
                ", eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", origin='" + origin + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return getTrimmedLine().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineModel lineModel = (LineModel) o;
        return eventName.equals(lineModel.eventName) &&
                ip.equals(lineModel.ip);
    }
}
