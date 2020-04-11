grammar Prometheus;

start: duration;

duration: LEFT_BRACE DURATION RIGHT_BRACE;

NUMBER: DEGIT+ PT DEGIT+
      | DEGIT+ PT
      | PT DEGIT+
      | DEGIT+
      | NAN
      | INF
      ;

DURATION: [1-9]+[msywh];

IDENTIFIER: [a-zA-Z1-9]+;
METRIC_IDENTIFIER: [a-zA-Z1-9]*':'[a-zA-Z1-9]+;

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
NAN: [nN][aA][nN];
INF: [iI][nN][fF];

ADD: '+';
SUB: '-';
//=!#$%&:;<>?@[\]^_`|~
STRING: [A-Za-z\\"'.`]+;
COMMENT: '#'[ A-Za-z1-9+]+;

fragment
  DEGIT: [0-9];
  PT: '.';
