package io.github.roycetech.junitcast.example;

public class RockPaperScissors {
	String battle(String left, String right)
	{
		if ("rock".equals(left) && "rock".equals(right) ||
			"paper".equals(left) && "paper".equals(right) ||
			"scissors".equals(left) && "scissors".equals(right)) return "draw";

		if ("rock".equals(left) && "scissors".equals(right) ||
			"paper".equals(left) && "rock".equals(right) ||
			"scissors".equals(left) && "paper".equals(right)) return "left";

		return "right";
	}
}
