package ASC26sep27;

public class TrazaMatriz {
	
	
	private static double recursivaTraza(double matriz[][], int indice) {
	
		// Caso base : hemos llegado al final de la diagonal
		
		if(indice == matriz.length)
			return 0;
		
		// Caso recrsivo: No hemos llegado al final de la diagonal
		
		return ( matriz[indice][indice] + recursivaTraza(matriz,indice+1) );
		
	}
	
	
	
	private static double calculaTrazaMatriz(double matriz[][]) {
		if( (matriz==null) || (matriz.length == 0 ) // es nula
			|| (matriz.length != (matriz[1]).length) ) // no es cuadrada
		{
		   throw new IllegalArgumentException("La matriz debe ser no nula o cuadrada"); 
		};
		
		return recursivaTraza(matriz, 0);  // empezamos con el primer elemento de la diagonal
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] matriz =
			{
				{1.0, 2,0, 3,0},
				{4.0, 5.0, 6.0},
				{7.0, 8.0, 9.0}
			};
		
		System.out.println((matriz[0]).length);
		System.out.println(matriz[1].length);
		System.out.println(matriz[2].length);
		
		double traza = calculaTrazaMatriz(matriz);
		System.out.println("La traza de la matriz es :"+ traza);

	}

}
