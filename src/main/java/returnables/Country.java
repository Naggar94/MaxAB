package returnables;

public class Country {
	String name;
	String continent;
	Long population;
	Double life_expectancy;
	String country_language;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public Long getPopulation() {
		return population;
	}
	public void setPopulation(Long population) {
		this.population = population;
	}
	public Double getLife_expectancy() {
		return life_expectancy;
	}
	public void setLife_expectancy(Double life_expectancy) {
		this.life_expectancy = life_expectancy;
	}
	public String getCountry_language() {
		return country_language;
	}
	public void setCountry_language(String country_language) {
		this.country_language = country_language;
	}
}
