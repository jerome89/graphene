grammar Prometheus;

tokens { DURATION }

@lexer::members {
  boolean isBracketOpen = false;
}

start: LEFT_BRACE;

duration: DURATION;

//start: mathFormulaExpr               # MathematicalFormula
//     | assignmentMathFormulaExpr     # AssignmentMathematicalFormula
//;
//
//mathFormulaExpr:
//      NUMBER {System.out.println("Number : ");}
//      | IDENTIFIER
//      | '-' mathFormulaExpr
//      | mathFormulaExpr ('*' | '/') mathFormulaExpr
//      | mathFormulaExpr ('+' | '-') mathFormulaExpr
//;
//
//assignmentMathFormulaExpr: IDENTIFIER '=' mathFormulaExpr
//;

NUMBER: [0-9]+;
IDENTIFIER: [a-z]+;

LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
COMMA: ',';
ASSIGN: '=';
COLON: ':';
SEMICOLON: ';';
BLANK: '_';
TIMES: 'x';
SPACE: '<space>';

//expression:
//                metric                    # ExpressionCall
//;
//
///*
// * Metric descriptions.
// */
//
//metric: metric_identifier label_set
//      | label_set
//;
//
//metric_identifier: MetricIdentifier;
//
//label_set: LEFT_BRACE label_set_list RIGHT_BRACE {System.out.println("Expression found : "); }
//         | LEFT_BRACE label_set_list COMMA RIGHT_BRACE {System.out.println("Expression found : "); }
//         | LEFT_BRACE RIGHT_BRACE {System.out.println("Expression found : "); }
//         | {System.out.println("Expression found : "); }
//;
//
//label_set_list: label_set_list COMMA label_set_item
//              | label_set_item
//;
//
//label_set_item: String EQL QoutedString
//;
//
//
//Boolean: TRUE | FALSE;
//Integer: '-'* DIGIT+;
//Float: '-'* DIGIT+ '.' DIGIT+;
//Scientific: (Integer | Float) ('e' | 'E') Integer;
//String: [a-zA-Z1-9]+;
//QoutedString: DoubleQuotedString | SingleQuotedString;
//DoubleQuotedString: '"'String'"';
//SingleQuotedString: '\''String'\'';
////FunctionName: [a-zA-Z_]+ [a-zA-Z_0-9]*;
//MetricIdentifier: [a-zA-Z_]+ [a-zA-Z_0-9]*;
////ValidChars: [=!#$%&*+\-/0123456789:;<>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz|~]+;
//EscapedChar: BACKSLASH SYMBOL;
//
//EQL: '=';
//DOT: '.';
//COMMA: ',';
//LEFT_BRACE: '{';
//RIGHT_BRACE: '}';
//LEFT_PAREN: '(';
//RIGHT_PAREN: ')';
//SYMBOL: [(){},=.'"];
//BACKSLASH: '\\';
//DIGIT: [0-9];
//WS: (' ' | '\t')+ -> skip;
//VALID_METRIC_CHAR: [!#$%&*+\-/0123456789,:;<>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]{}^_`abcdefghijklmnopqrstuvwxyz|~];
//TRUE: ('t' | 'T')('r' | 'R')('u' | 'U')('e' | 'E');
//FALSE: ('f' | 'F')('a' | 'A')('l' | 'L')('s' | 'S')('e' | 'E');
//

