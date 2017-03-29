package pro.kondratev.xlsxpoiexample;

/**
 * Created by sudhanshrana on 19/07/16.
 */
public class Lecture {
    String CCode,Room,Teacher;

    Lecture()
    {
        CCode=null;
        Room=null;
        Teacher=null;
    }

    public String getCCode() {
        return CCode;
    }

    public void setCCode(String CCode) {
        this.CCode = CCode;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }
}
