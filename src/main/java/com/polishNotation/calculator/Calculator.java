package com.polishNotation.calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private int getPriority(String operator){
        if (operator.equals("+") || operator.equals("-")) return 2;
        else if (operator.equals("*") || operator.equals("/")) return 3;
        else if (operator.equals("(")) return 1;
        else if (operator.equals(")")) return -1;
        else return 0;
    }

    private Stack<Double> matchOperation(Stack<Double> num, Stack<String> act) throws ArithmeticException {

        double firstOperand;
        double secondOperand;

        secondOperand = num.pop();
        firstOperand = num.pop();

            switch (act.peek()){
                case "+" :
                    num.push(firstOperand+secondOperand);
                    break;
                case "-" :
                    num.push(firstOperand-secondOperand);
                    break;
                case "*" :
                    num.push(firstOperand*secondOperand);
                    break;
                case "/":
                    if(num.push(firstOperand/secondOperand) != Double.POSITIVE_INFINITY)
                        num.push(firstOperand/secondOperand);
                    else
                        throw new ArithmeticException();
                    break;
            }

        return num;
    }
    private String translateInPolishNotation(List<String> endList){
        Stack<Double> numStack = new Stack<>();
        Stack<String> actStack = new Stack<>();
        try {
            int priority;
            for (int i = 0; i < endList.size(); i++) {
                priority = getPriority(endList.get(i));

                switch (priority) {
                    case 0:
                        numStack.push(Double.valueOf(endList.get(i)));
                        break;
                    case 1:
                        actStack.push(endList.get(i));
                        break;
                    case 2:
                    case 3:
                        while (!actStack.empty()) {
                            if (getPriority(actStack.peek()) >= priority) {
                                matchOperation(numStack, actStack);
                                actStack.pop();
                            } else break;
                        }
                        actStack.push(endList.get(i));
                        break;
                    case -1:
                        while (!actStack.empty()) {
                            if (getPriority(actStack.peek()) != 1) {
                                matchOperation(numStack, actStack);
                                actStack.pop();
                            } else {
                                actStack.pop();
                                break;
                            }
                        }
                        break;
                }
            }
            while (!actStack.empty()) {
                matchOperation(numStack, actStack);
                actStack.pop();
            }
        }catch (Exception e){
            return null;
        }

        double lastValue = numStack.pop().doubleValue();
       if(lastValue%1 == 0)
            return String.valueOf((int)lastValue);

        return String.valueOf(lastValue);
    }




    private List<String> changeString(String statement)  {
        List<String> endList = new ArrayList<>();

        try {
            Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)|([+\\-*/()])");

            Matcher matcherNum = pattern.matcher(statement);

            while (matcherNum.find())
                endList.add(matcherNum.group());
            if((statement.indexOf(',')!=-1)||(statement.indexOf("..")!=-1)||(statement.equals(""))||(statement==null))
                throw new Exception();
        }catch (Exception e){
            return null;
        }







        return endList;
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */


    public String evaluate(String statement) {

        return translateInPolishNotation(changeString(statement));
    }


}
