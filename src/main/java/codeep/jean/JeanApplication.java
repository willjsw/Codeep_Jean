package codeep.jean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JeanApplication {
	private static String[] day ={"MON","TUE","WED","THR","FRI","SAT","SUN"};
	private static String[] time = {"8A","9A","10A","11A","12A","13A","14A","15A","16A","17A","18A","19A","20A","21A","22A","23A",
			"8B","9B","10B","11B","12B","13B","14B","15B","16B","17B","18B","19B","20B","21B","22B","23B"};

	public static void main(String[] args) {
		SpringApplication.run(JeanApplication.class, args);

		for(int i=0; i<day.length;i++){
			for(int j=0; j<time.length;j++){
				System.out.println(day[i]+"_"+time[j]+",");
			}
		}


	}



}
