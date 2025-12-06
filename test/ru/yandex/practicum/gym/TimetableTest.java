package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, getTrainCount(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)));

        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, getTrainCount(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY)));

    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1,getTrainCount(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)));

        // Проверить, что за четверг вернулось два занятия
        Assertions.assertEquals(2,getTrainCount(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)));

        // Проверить, что в правильном порядке: сначала в 13:00, потом в 20:00
        Assertions.assertEquals(new TimeOfDay(13, 0),
                                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).navigableKeySet().getFirst());
        Assertions.assertEquals(new TimeOfDay(20, 0),
                                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).navigableKeySet().getLast());

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0,getTrainCount(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY)));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1,
                timetable.getTrainingSessionsForDayAndTime( DayOfWeek.MONDAY,
                                                            new TimeOfDay(13,00)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertEquals(0,
                timetable.getTrainingSessionsForDayAndTime( DayOfWeek.MONDAY,
                        new TimeOfDay(14,00)).size());
    }

    @Test
    void testGetTrainingCountByCouch() {
        Timetable timetable = new Timetable();

        Group group = new Group("Аэробика для детей", Age.CHILD, 60);
        Coach coachFst = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSessionFst = new TrainingSession(group, coachFst,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSessionSec = new TrainingSession(group, coachFst,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSessionFst);
        //Добавим 1 тренировку, проверим, что количество верно
        Assertions.assertEquals(1, timetable.getCountByCoaches().get(coachFst));

        timetable.addNewTrainingSession(singleTrainingSessionSec);
        //добавим ещё 1 тренировку, проверим, что количество изменилось
        Assertions.assertEquals(2, timetable.getCountByCoaches().get(coachFst));
    }

    @Test
    void testOrderOfCouchesInTrainCountList() {
        Timetable timetable = new Timetable();

        Group group = new Group("Аэробика для детей", Age.CHILD, 60);
        Coach coachFst = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSessionFst = new TrainingSession(group, coachFst,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSessionSec = new TrainingSession(group, coachFst,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSessionFst);
        timetable.addNewTrainingSession(singleTrainingSessionSec);

        group = new Group("Аэробика для взрослых", Age.CHILD, 60);
        Coach coachTrd = new Coach("Залесский", "Семён", "Павлович");
        singleTrainingSessionFst = new TrainingSession(group, coachTrd,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(singleTrainingSessionFst);

        Coach coachSec = new Coach("Семёнова", "Виктория", "Владимировна");
        singleTrainingSessionFst = new TrainingSession(group, coachSec,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        singleTrainingSessionSec = new TrainingSession(group, coachSec,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));
        TrainingSession singleTrainingSessionTrd = new TrainingSession(group, coachSec,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(singleTrainingSessionFst);
        timetable.addNewTrainingSession(singleTrainingSessionSec);
        timetable.addNewTrainingSession(singleTrainingSessionTrd);

        //Это просто чтоб в консоли список посмотреть
        for (Coach coach : timetable.getCountByCoaches().keySet()){
            System.out.println("Тренер " + coach.getSurname() + " " + coach.getName()
                                + " сегодня имеет тренировок: " +
                                timetable.getCountByCoaches().get(coach));
        }

        Map<Coach,Integer> trainCounts =  timetable.getCountByCoaches();
        Iterator<Map.Entry<Coach, Integer>> iterator = trainCounts.entrySet().iterator();

        //Проверим, что в возвращаемом списке - на первом месте  - тренер с 3мя тренировками
        Map.Entry<Coach,Integer> currEntry = iterator.next();
        Assertions.assertEquals(coachSec, currEntry.getKey());

        //Проверим, что на втором месте тренер с 2 тренировками
        currEntry = iterator.next();
        Assertions.assertEquals(coachFst, currEntry.getKey());

        //Проверим, что на последнем месте - тренер с 1 тренировкой
        currEntry = iterator.next();
        Assertions.assertEquals(coachTrd, currEntry.getKey());

    }

    //напишем вспомогательный метод, подсчитывающий количество тренировок с учетом вложенной структуры
    private static int getTrainCount(TreeMap<TimeOfDay,ArrayList<TrainingSession>> trainsInDay){
        if (trainsInDay == null) {
            return 0;
        }
        int count = 0;
        for (TimeOfDay td : trainsInDay.keySet()){
            count += trainsInDay.get(td).size();
        }
        return count;
    }

}
