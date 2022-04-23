package sleeplessdevelopers.schedulecreator;

public enum Day {

    M, T, W, R, F, NULL;

    public static Day Day(char c) {
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
                return Day.NULL;
        }
    }

    public static String toDayString(Day d) {
        switch (d) {
            case M:
                return "Mon";
            case T:
                return "Tue";
            case W:
                return "Wed";
            case R:
                return "Thu";
            case F:
                return "Fri";
            default:
                return "NULL";
        }
    }
}


    //add fix for course.todaystringlist

//    //temp method
//	public ArrayList<String> getDayStringList(){
//		ArrayList<String> stringList = new ArrayList<>();
//		for(int x = 0; x<days.size(); x++){
//			stringList.add(String.valueOf((daysToString().charAt(x))));
//			if(stringList.get(x).equalsIgnoreCase("M")){
//				stringList.remove(x);
//				stringList.add(x,"Mon");
//			}else if(stringList.get(x).equalsIgnoreCase("T")){
//				stringList.remove(x);
//				stringList.add(x,"Tue");
//			}else if(stringList.get(x).equalsIgnoreCase("W")){
//				stringList.remove(x);
//				stringList.add(x,"Wed");
//			}else if(stringList.get(x).equalsIgnoreCase("R")){
//				stringList.remove(x);
//				stringList.add(x,"Thu");
//			}else if(stringList.get(x).equalsIgnoreCase("F")){
//				stringList.remove(x);
//				stringList.add(x,"Fri");
//			}
//		}
//		return stringList;
//	}
