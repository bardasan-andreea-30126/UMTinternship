import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeetingSchedule {

    public static List<String[]> searchAvailableTimes(String[][] calendar1, String[] limits1,
                                                      String[][] calendar2, String[] limits2,
                                                      int meetingTimeMin) {
        LocalTime minTime1 = LocalTime.parse(limits1[0], DateTimeFormatter.ofPattern("H:mm"));
        LocalTime maxTime1 = LocalTime.parse(limits1[1], DateTimeFormatter.ofPattern("H:mm"));
        LocalTime minTime2 = LocalTime.parse(limits2[0], DateTimeFormatter.ofPattern("H:mm"));
        LocalTime maxTime2 = LocalTime.parse(limits2[1], DateTimeFormatter.ofPattern("H:mm"));

        List<LocalTime[]> availableRanges1 = getAvailableRanges(calendar1, minTime1, maxTime1);
        List<LocalTime[]> availableRanges2 = getAvailableRanges(calendar2, minTime2, maxTime2);

        List<LocalTime[]> availableRanges = getIntersection(availableRanges1, availableRanges2);

        List<String[]> availableTimes = new ArrayList<>();
        for (LocalTime[] range : availableRanges) {
            LocalTime start = range[0];
            LocalTime end = range[1];
            if (start.plusMinutes(meetingTimeMin).isBefore(end)) {
                availableTimes.add(new String[]{start.toString(), end.toString()});
            }
        }
        return availableTimes;
    }

    private static List<LocalTime[]> getAvailableRanges(String[][] calendar, LocalTime minTime, LocalTime maxTime) {
        List<LocalTime[]> availableRanges = new ArrayList<>();
        LocalTime endTime = minTime;
        for (String[] event : calendar) {
            LocalTime startTime = LocalTime.parse(event[0], DateTimeFormatter.ofPattern("H:mm"));
            if (startTime.isAfter(endTime)) {
                availableRanges.add(new LocalTime[]{endTime, startTime});
            }
            endTime = LocalTime.parse(event[1], DateTimeFormatter.ofPattern("H:mm"));
        }
        if (endTime.isBefore(maxTime)) {
            availableRanges.add(new LocalTime[]{endTime, maxTime});
        }
        return availableRanges;
    }

    private static List<LocalTime[]> getIntersection(List<LocalTime[]> ranges1, List<LocalTime[]> ranges2) {
        List<LocalTime[]> intersection = new ArrayList<>();
        for (LocalTime[] range1 : ranges1) {
            for (LocalTime[] range2 : ranges2) {
                LocalTime start = range1[0].isBefore(range2[0]) ? range2[0] : range1[0];
                LocalTime end = range1[1].isBefore(range2[1]) ? range1[1] : range2[1];
                if (start.isBefore(end) && !start.equals(end)) {
                    intersection.add(new LocalTime[]{start, end});
                }
            }
        }
        return intersection;
    }
    public static void main(String[] args)
    {

        String[][] calendar1 = {{"9:00", "10:30"}, {"12:00", "13:00"}, {"16:00", "18:00"}};
        String[][] calendar2 =  {{"10:00", "11:30"}, {"12:30", "14:30"}, {"14:30", "15:00"}, {"16:00", "17:00"}};
        String[] limits1 = {"9:00", "20:00"};
        String[] limits2 = {"10:00", "18:30"};
        int meetingTimeMin = 30;

        List<String[]> availableTimes =  searchAvailableTimes(calendar1, limits1, calendar2, limits2, meetingTimeMin);

        System.out.println("Available meeting times:");
        if (availableTimes.isEmpty()) {
            System.out.println("None");
        }
        else
        {
            for (String[] time : availableTimes) {
                System.out.println("[" + "['" + time[0] + "', '" + time[1] + "']" + "]");
            }
        }
    }
}




