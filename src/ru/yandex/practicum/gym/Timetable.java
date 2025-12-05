package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private HashMap<DayOfWeek,TreeMap<TimeOfDay,ArrayList<TrainingSession>>> timetable = new HashMap<>();
    private HashMap<Coach, Integer> countOfTrains = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {

        //Если дня ещё нет - строим всю иерархию (день-время-список тренировок)
        if (!timetable.containsKey(trainingSession.getDayOfWeek())) {

            TreeMap<TimeOfDay,ArrayList<TrainingSession>> trainInDay = new TreeMap<>();
            ArrayList<TrainingSession> listOfTrains = new ArrayList<>();

            listOfTrains.add(trainingSession);
            trainInDay.put(trainingSession.getTimeOfDay(),listOfTrains);
            timetable.put(trainingSession.getDayOfWeek(),trainInDay);
        } else {
            //если день уже есть - проверяем - есть ли уже ключ со временем - если да - просто
            //добавляем в него в список - ещё одну тренировку
            TreeMap<TimeOfDay,ArrayList<TrainingSession>> trainInDay = timetable.get(trainingSession.getDayOfWeek());
            if (trainInDay.containsKey(trainingSession.getTimeOfDay())) {
               trainInDay.get(trainingSession.getTimeOfDay()).add(trainingSession);
            } else {
                //если такого времени в этот день еще не было - добавляем время в ключ и в значение -
                //новый список тренировок с нашим названием тренировки
                ArrayList<TrainingSession> listOfTrains = new ArrayList<>();

                listOfTrains.add(trainingSession);
                trainInDay.put(trainingSession.getTimeOfDay(),listOfTrains);
            }
        }

        //Соберем тренировки в хэш-таблицу
        countOfTrains.put(trainingSession.getCoach(),
                countOfTrains.getOrDefault(trainingSession.getCoach(),0) + 1);
    }

    public TreeMap<TimeOfDay,ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //чтобы сразу можно было использовать size() - защитимся от null значения по ключу
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> result = timetable.get(dayOfWeek);
        return result != null ? result : new TreeMap<>();
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //чтобы сразу можно было использовать size() - защитимся от null значения по ключу
        ArrayList<TrainingSession> result = timetable.get(dayOfWeek).get(timeOfDay);
        return result != null ? result : new ArrayList<>();
    }

    public Map<Coach,Integer> getCountByCoaches() {
        /*для возврата в виде отсортированного по убыванию количества тренировок списка
        возьмем исходную мапу, в которую собираем данные при каждом создании тренировки,
        отсортируем её записи по значению в обратном порядке, а результат подсобирем в
        LinkedHashMap, поскольку это сохранит порядок добавления */

        return countOfTrains.entrySet()
                .stream()
                .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

}
