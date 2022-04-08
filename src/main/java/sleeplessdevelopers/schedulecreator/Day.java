package sleeplessdevelopers.schedulecreator;

public enum Day {

    M, T, W, R, F, NULL;

    public static Day getDay(char c) {
        switch (c) {
            case 'M':
                return Day.M;
            case 'T':
                return Day.T;
            case 'W':
                return Day.W;
            case 'R':
                return Day.R;
            case 'F':
                return Day.F;
            default:
                return null;
        }
    }

    //add fix for course.todaystringlist

}
