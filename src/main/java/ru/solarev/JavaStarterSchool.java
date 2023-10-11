package ru.solarev;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс с основной логикой базы данных
 */
public class JavaStarterSchool {
    private List<Map<String, Object>> hashTable;

    public JavaStarterSchool() {
        hashTable = new ArrayList<>();
    }

    public List<Map<String, Object>> execute(String request) throws Exception{
        //INSERT VALUES 'lastName' = 'Fedorov', 'id' = 3, 'age' = 40, 'active' = true;
        findCommand(request);
        return hashTable;
    }

    private void findCommand(String request) {
        String newStr = request.toUpperCase();
        if (newStr.toUpperCase().contains("INSERT")){
            System.out.println("Insert");
            runInsert(request);
        } else if (newStr.contains("UPDATE")){
            System.out.println("Update");
            runUpdate(request);
        } else if (newStr.contains("DELETE")){
            System.out.println("Delete");
            runDelete(request);
        } else if (newStr.contains("SELECT")){
            System.out.println("Select");
            runSelect(request);
        }else {
            System.out.println("Command not found");
        }
    }

    private void runInsert(String request) {
        String str = request.replaceAll(" ", "").replaceAll(",", "");;
        Map<String, Object> map = new HashMap<>();
        if (str.toLowerCase().contains("'lastname'")) {
            String s = str.substring(str.indexOf("'lastName'="));
            String[] temp = s.split("'");
            map.put("lastName", temp[3]);
        } else map.put("lastName", null);
        if (str.toLowerCase().contains("'id'")) {
            String s = str.substring(str.indexOf("'id'=")).replaceAll("=", "'");
            String[] temp = s.split("'");
            map.put("id", Integer.parseInt(temp[3]));
        } else map.put("id", null);
        if (str.toLowerCase().contains("'age'")) {
            String s = str.substring(str.indexOf("'age'=")).replaceAll("=", "'");
            String[] temp = s.split("'");
            map.put("age", Integer.parseInt(temp[3]));
        } else map.put("age", null);
        if (str.toLowerCase().contains("'cost'")) {
            String s = str.substring(str.indexOf("'cost'=")).replaceAll("=", "'");
            String[] temp = s.split("'");
            map.put("cost", Double.parseDouble(temp[3]));
        } else map.put("cost", null);
        if (str.toLowerCase().contains("'active'")) {
            String s = str.substring(str.indexOf("'active'=")).replaceAll("=", "'");
            String[] temp = s.split("'");
            map.put("active", Boolean.parseBoolean(temp[3]));
        }else map.put("active", false);
        hashTable.add(map);

    }

    private void runUpdate(String request) {
        String[] str = request.split("where");

        String update = str[0];
        String idStr = str[1];
        System.out.println(update);
        System.out.println(idStr);
        String[] updateParam = new String[2];

        if (idStr.toLowerCase().contains("'id'")) {
            String param = "'id'";
            choiceUpdateField(param, idStr, updateParam, update);
        }
        if (idStr.toLowerCase().contains("'lastname'")) {
            String param = "'lastname'";
            choiceUpdateField(param, idStr, updateParam, update);
        }
        if (idStr.toLowerCase().contains("'age'")) {
            String param = "'age'";
            choiceUpdateField(param, idStr, updateParam, update);
        }
        if (idStr.toLowerCase().contains("'cost'")) {
            String param = "'cost'";
            choiceUpdateField(param, idStr, updateParam, update);
        }
        if (idStr.toLowerCase().contains("'active'")) {
            String param = "'active'";
            choiceUpdateField(param, idStr, updateParam, update);
        }
    }

    private void choiceUpdateField(String param, String idStr, String[] updateParam, String update) {
        String s = idStr.substring(idStr.indexOf(param+"=")).replaceAll("=", "'");
        String[] temp = s.split("'");
        updateParam[0] = param;
        updateParam[1] = temp[3];
        updateSelect(update, updateParam);
    }

    private void updateSelect(String update, String[] updateParam) {
        for (Map<String, Object> map: hashTable) {
            String temp1 = updateParam[0].replaceAll("'", "");
            updateParam[0] = temp1;
            if (map.get(updateParam[0]).equals(Integer.parseInt(updateParam[1]))){
                if (update.toLowerCase().contains("'lastname'")) {
                    String s = update.substring(update.indexOf("'lastName'="));
                    String[] temp = s.split("'");
                    map.put("lastName", temp[3]);
                }
                if (update.toLowerCase().contains("'age'")) {
                    String s = update.substring(update.indexOf("'age'=")).replaceAll("=", "'");
                    String[] temp = s.split("'");
                    map.put("age", Integer.parseInt(temp[3]));
                }
                if (update.toLowerCase().contains("'cost'")) {
                    String s = update.substring(update.indexOf("'cost'=")).replaceAll("=", "'");
                    String[] temp = s.split("'");
                    map.put("cost", Double.parseDouble(temp[3]));
                }
                if (update.toLowerCase().contains("'active'")) {
                    String s = update.substring(update.indexOf("'active'=")).replaceAll("=", "'");
                    String[] temp = s.split("'");
                    map.put("active", Boolean.parseBoolean(temp[3]));
                }
                System.out.println(map);
            }
        }
    }

    private void runDelete(String request) {
        String[] str = request.split("WHERE");
        String idStr = str[1];
        if (idStr.toLowerCase().contains("'id'")) {
            String s = idStr.replaceAll("=", "'").replaceAll(" ", "");
            String[] temp = s.split("'");
            hashTable.removeIf(map -> map.get("id").equals(Integer.parseInt(temp[3])));
        } else System.out.println("Request contains error");

    }

    private void runSelect(String request) {
        //Select WHERE 'age'>=30 and 'lastName' ilike '%F%'
//        String[] oper = {"ilike", "like"};
        if (request.contains("and") || request.contains("or")) {
            String[] str = request.toLowerCase().split("where|and|or");
            List<Map<String, Object>> whereList = selectStandart("where " + str[1]);
            List<Map<String, Object>> andList = selectStandart("where " + str[2]);
            if (request.contains("and")) {
                whereList.retainAll(andList);
                whereList.forEach(System.out::println);
            } else if (request.contains("or")){
                whereList.addAll(andList);
                whereList.forEach(System.out::println);
            }
        } else {
            selectStandart(request);
        }
    }

    private List<Map<String, Object>> selectStandart(String request) {
        List<Map<String, Object>> selectList = new ArrayList<>();
        //"Select WHERE 'age'>=30"
        String[] str = request.replaceAll(" ", "").toLowerCase().split("where");
        String where = str[1];
        String param = "";
        if (where.toLowerCase().contains("'id'")) {
            param = "id";
            String[] s = where.split(">=|<=|>|<|==|!=");
            System.out.println(Integer.parseInt(s[1]));
            if (where.contains(">=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c >= Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c <= Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains(">")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c > Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c < Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("==")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c == Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("!=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c != Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            }
        }
        if (where.contains("'lastname'")) {
            //"Select WHERE 'lastName' like %rov"
            param = "lastName";
            String[] s = where.split("ilike|like|%|==");
            String c = "";
            if (where.contains("ilike")) {
                for (Map<String, Object> map: hashTable) {
                    c = String.valueOf(map.get(param));
                    if (!c.contains(s[2])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("like")) {
                for (Map<String, Object> map: hashTable) {
                    c = String.valueOf(map.get(param));
                    if (c.contains(s[2])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("==")) {
                String[] temp = s[1].split("'");
                String name = temp[1].substring(0,1).toUpperCase() + temp[1].substring(1);
                for (Map<String, Object> map: hashTable) {
                    c = String.valueOf(map.get(param));
                    if (Objects.equals(c, name)) {
                        selectList.add(map);
                    }
                }
            }
        }
        if (where.toLowerCase().contains("'age'")) {
            param = "age";
            String[] s = where.split(">=|<=|>|<|==|!=");
            if (where.contains(">=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c >= Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c <= Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains(">")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c > Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c < Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("==")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c == Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("!=")){
                for (Map<String, Object> map: hashTable) {
                    int c = (int) map.get(param);
                    if (c != Integer.parseInt(s[1])) {
                        selectList.add(map);
                    }
                }
            }
        }
        if (where.toLowerCase().contains("'cost'")) {
            param = "cost";
            String[] s = where.split(">=|<=|>|<|==|!=");
            if (where.contains(">=")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<=")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains(">")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("<")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("==")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("!=")){
                for (Map<String, Object> map: hashTable) {
                    double c = (double) map.get(param);
                    if (c >= Double.parseDouble(s[1])) {
                        selectList.add(map);
                    }
                }
            }
        }
        if (where.toLowerCase().contains("'active'")) {
            param = "active";
            String[] s = where.split("==|!=");
            String[] temp = s[1].split("'");
            if (where.contains("==")){
                for (Map<String, Object> map: hashTable) {
                    boolean c = (boolean) map.get(param);
                    if (c == Boolean.parseBoolean(temp[1])) {
                        selectList.add(map);
                    }
                }
            } else if (where.contains("!=")){
                for (Map<String, Object> map: hashTable) {
                    boolean c = (boolean) map.get(param);
                    if (c == Boolean.parseBoolean(temp[1])) {
                        selectList.add(map);
                    }
                }
            }
        }
        return selectList;
    }


    public List<Map<String, Object>> getHashTable() {
        return hashTable;
    }

    public void setHashTable(List<Map<String, Object>> hashTable) {
        this.hashTable = hashTable;
    }

    public void printTable() {
        for (Map<String, Object> map: hashTable) {
            System.out.println("[" + map.get("id") + ", " +  map.get("lastName") + ", " +  map.get("age") +
                    ", " +  map.get("cost") + ", " +  map.get("active"));
        }
    }
}
