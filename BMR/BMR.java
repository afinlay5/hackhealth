import static java.lang.System.out;


/*
	BASAL METABOLIC RATE
	
	MIFFLIN ST. JEOR EQUATION
	
	MEN		10 x Weight(kg) + 6.25 x Height(cm) – 4.92 x Age + 5
	WOMEN	10 x Weight(kg) + 6.25 x Height(cm) – 4.92 x Age - 161

*/


class BMRException extends Exception {
	
	public BMRException () { out.println("A generic BMR Exception has occurred."); }
	public BMRException (String message) {	super(message); }
	
}


class BMR {

	// Gender, Weight, Height, Age	
	public static double getBMR (String gender, double weight, double height, int age ) throws BMRException {
		
		double result = 0.0;	// Remember to check for 0.0!
		
		// Begin BMR Calc
		final int coeff_1 = 10;
		final double coeff_2 = 6.25;
		final double coeff_3 = 4.92;
		final int coeff_M = 5, coeff_F = 161;
		final double cm = 2.54;
		final double kilograms = 2.20462;
			
		double weight_kg = weight / kilograms; 
		double height_cm = height*cm;
		
		
		if (gender == "MALE")
			//Finish
			result = coeff_1 * weight_kg + coeff_2 * height_cm - coeff_3 * age + coeff_M;
		else if (gender == "FEMALE")
			//Finish
			result = coeff_1 * weight_kg + coeff_2 * height_cm - coeff_3 * age - coeff_F;
		else
			System.out.println("How the devil did you accomplish this?");
		
		if (result == 0.0)
			throw new BMRException("Your BMR Result is invalid");
			
		return result;
	} 
	
	
	
	
	
}

