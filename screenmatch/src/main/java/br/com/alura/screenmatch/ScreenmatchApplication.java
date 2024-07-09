package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConvert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ApiConsumption apiConsumption = new ApiConsumption();
		var json = apiConsumption.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=b740336e");
		System.out.println(json);

		DataConvert converter = new DataConvert();
		SeriesData data = converter.getData(json, SeriesData.class);
		System.out.println(data);
	}
}
