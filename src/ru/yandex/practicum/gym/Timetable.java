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
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> result = timetable.get(dayOfWeek);
        if (result == null) {
            return new TreeMap<>();
        }
        //Возвращаем не оригинальную структуру, а копию
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> retCopyMap = new TreeMap<>();
        for (Map.Entry<TimeOfDay, ArrayList<TrainingSession>> entry : result.entrySet()) {
            // Копируем каждый список отдельно
            ArrayList<TrainingSession> listCopy = new ArrayList<>(entry.getValue());
            retCopyMap.put(entry.getKey(), listCopy);
        }

        return retCopyMap;
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        /*чтобы на строке timetable.get(dayOfWeek).get(timeOfDay); не поймать nullPointerException
        в результате отсутствия данных за день - разделим на 2 шага выборку и отдельно проверим - есть ли данные
        для этого дня, если да - идем дальше, если нет - возвращаем пустую коллекцию, и потом - если данные найдены -
        идем по ключу "время тренировки"
         */

        // Получаем исходную структуру для дня
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        // Если день не найден или время не найдено — возвращаем пустой список
        if (dayMap == null) {
            return new ArrayList<>();
        }

        ArrayList<TrainingSession> result = dayMap.get(timeOfDay);
        if (result == null) {
            return new ArrayList<>();
        }

        // Возвращаем копию списка
        return new ArrayList<>(result);
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
