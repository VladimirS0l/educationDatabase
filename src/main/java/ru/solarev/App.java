package ru.solarev;

/**
 * Класс для работы с запросами SELECT, INSERT, UPDATE, DELETE
 */
public class App 
{
    public static void main( String[] args ) {
        JavaStarterSchool jss = new JavaStarterSchool();
        try {
            //Добавление в базу данных людей
            jss.execute("INSERT VALUES 'lastName' = 'Fedorov'," +
                    " 'id' = 3, 'cost' = 40.2, 'age' = 40, 'active' = true");
            jss.execute("INSERT VALUES 'lastName' = 'Solarev'," +
                    " 'id' = 1, 'cost' = 51.7, 'age' = 30, 'active' = false");
            //Распечатать всю базу данных
            jss.printTable();
            //Обновить строку
            jss.execute("UPDATE VALUES 'active'=false, 'cost'=10 where 'age'=20");
            jss.printTable();
            //Удалить строку
            jss.execute("DELETE WHERE 'id' = 3");
            jss.printTable();
            //Поиск в базе данных по параметрам
            jss.execute("Select WHERE 'age'>=30 and 'lastName' ilike '%F%'");
            jss.execute("Select WHERE 'active' == 'false' or 'id' == 3");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
