public class LineModel {
    //Time,Event context,Component,Event name,Description,Origin,IP address
    //"3/11/18, 14:41",Course: VVPS,Logs,Log report viewed,The user with id '2' viewed the log report for the course with id '49'.,web,212.5.158.162

    private String time;
    private String eventContext;
    private String component;
    private String eventName;
    private String description;
    private String origin;
    private String ip;

    private String line;

    public String getTime() {
        return time;
    }

    public String getEventContext() {
        return eventContext;
    }

    public String getComponent() {
        return component;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getOrigin() {
        return origin;
    }

    public String getIp() {
        return ip;
    }

    public String getLine() {
        return line;
    }

    public LineModel(String line) {
        line = line.replaceAll("�+.*�", "");
        this.line = line;
        String trimmedLine;
        int lastIndex = line.indexOf("\"", 2);
        time = line.substring(0, lastIndex + 1);
        trimmedLine = line.substring(lastIndex + 2);
        int firstIndex = trimmedLine.indexOf(',');
        if (!validateIndex(firstIndex, trimmedLine)) return;

        eventContext = trimmedLine.substring(0, firstIndex );
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');
        if (!validateIndex(firstIndex, trimmedLine)) return;

        component = trimmedLine.substring(0, firstIndex);
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');
        if (!validateIndex(firstIndex, trimmedLine)) return;

        eventName = trimmedLine.substring(0, firstIndex);
        eventName = eventName.replace(",", "");
        eventName = eventName.replace(" ", "|");
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');
        if (!validateIndex(firstIndex, trimmedLine)) return;

        description = trimmedLine.substring(0, firstIndex);
        trimmedLine = trimmedLine.substring(firstIndex + 1);
        firstIndex = trimmedLine.indexOf(',');
        if (!validateIndex(firstIndex, trimmedLine)) return;

        origin = trimmedLine.substring(0, firstIndex);
        trimmedLine = trimmedLine.substring(firstIndex + 1);

        ip = trimmedLine;
        ip = ip.replace(",", "");
    }

    String getTrimmedLine() {
        return this.eventName;
    }

    private boolean validateIndex(int index, String string) {
        return index >= 0 && string.length() > index;
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
