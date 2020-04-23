package cn.k.mybatis_learn;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.k.mybatis_learn.domain.StudentDomain;
import cn.k.mybatis_learn.mapper.StudentMapper;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        InputStream resourceAsStream = App.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession openSession = sqlSessionFactory.openSession();
        StudentMapper mapper = openSession.getMapper(StudentMapper.class);
//        List<StudentDomain> selectById = mapper.selectById();
//        for(StudentDomain sd:selectById){
//        	System.out.println(sd.toString());
//        }
//        System.out.println("------------------------");
//        List<Object> selectList2 = openSession.selectList("cn.k.mybatis_learn.mapper.StudentMapper.selectById");
//        for(Object sd:selectList2){
//        	System.out.println(sd.toString());
//        }

//        StudentDomain sddd = new StudentDomain();
//        sddd.setName("king");
//        mapper.insertkkk(sddd);
//        openSession.commit();
//        System.out.println(sddd.getId());


//        StudentDomain sd1 = new StudentDomain();
//        sd1.setName("1");
//
//        StudentDomain sd2 = new StudentDomain();
//        sd2.setName("2");
//
//        StudentDomain sd3 = new StudentDomain();
//        sd3.setName("3");
//        List<StudentDomain> sts = new ArrayList<StudentDomain>();
//        sts.add(sd1);
//        sts.add(sd2);
//        sts.add(sd3);
//        mapper.insertList(sts);
//        openSession.commit();

//        StudentDomain stu = new StudentDomain();
////        stu.setId(123456);
//        stu.setName("student");
//        List<StudentDomain> selectById = mapper.queryById(stu);
//        for(StudentDomain sd:selectById){
//        	System.out.println(sd.toString());
//        }

//        StudentDomain stu = new StudentDomain();
//        stu.setId(1);
////        stu.setAge(22);
//        stu.setName("student");
//        mapper.update(stu);

//        List<Integer> lists = new ArrayList<Integer>();
//        lists.add(1);
//        lists.add(2);
//        List<StudentDomain> selectById = mapper.queryByIdForeach(lists);
//        for (StudentDomain sd : selectById) {
//            System.out.println(sd.toString());
//        }
//        Integer[]a  = {1,2,3};
//        List<StudentDomain> selectById = mapper.queryByIdForeach2(a);
//        for (StudentDomain sd : selectById) {
//            System.out.println(sd.toString());
//        }


//        HashMap<String,Integer> map = new HashMap<String, Integer>();
//        map.put("K1",1);
//        map.put("K2",2);
//        map.put("K3",3);
//        map.put("K4",4);
//        List<StudentDomain> selectById = mapper.queryByIdForeach3(map);
//        for (StudentDomain sd : selectById) {
//            System.out.println(sd.toString());
//        }

//        HashMap<String, Object> selectresultmap = mapper.selectresultmap();
//        for(String k:selectresultmap.keySet()){
//            System.out.println(k+"---"+selectresultmap.get(k));
//        }

        StudentDomain sdd = new StudentDomain();
        sdd.setName("小老虎");
        sdd.setAge(23);
        mapper.insertTrim(sdd);
        openSession.commit();
        openSession.close();
    }
}
