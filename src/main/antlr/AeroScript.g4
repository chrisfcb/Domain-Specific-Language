grammar AeroScript;

@header {
package no.uio.aeroscript.antlr;
}

// Whitespace and comments added
WS           : [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT      : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

LCURL   : '{';
RCURL   : '}';
LSQUARE : '[';
RSQUARE : ']';
LPAREN  : '(';
RPAREN  : ')';

NEG     : '--';
SEMI    : ';';
COMMA   : ',';
GREATER : '>';

PLUS    : '+';
MINUS   : '-';
TIMES   : '*';

ARROW: '->';
ON: 'on';
OBSTACLE: 'obstacle';
BATTERY: 'low battery';
BASE: 'return to base';
MOVE: 'move'; 
MESSAGE: 'message';
SPEED: 'at speed';
TURN: 'turn';
RIGHT: 'right';
LEFT: 'left';
ASCEND: 'ascend by';
DESCEND: 'descend by';
FOR: 'for';
S: 's';
TO: 'to';
BY: 'by';
GROUND: 'descend to ground';

// Define all the elements of the language for the various keywords that you need
RANDOM  : 'random';
POINT   : 'point';

// Keywords
ID: [a-zA-Z_]+; // Allow underscore in ID
NUMBER: '-'?[0-9]+('.'[0-9]+)?;

// Entry point
program : (execution)+;
execution : ARROW? ID LCURL (statement)* RCURL (ARROW secondId=ID)? ;
statement : action | reaction | execution;

reaction : ON event ARROW ID;
event : OBSTACLE | BATTERY | MESSAGE LSQUARE ID RSQUARE;
action : (acDock | acMove | acTurn | acAscend | acDescend)
        (FOR expression S | SPEED expression)?;

acDock: BASE;
acMove: MOVE (TO POINT point | BY NUMBER);
acTurn : TURN (RIGHT | LEFT)? BY expression;
acAscend : ASCEND expression;
acDescend : DESCEND expression | GROUND;

expression : expression (PLUS | MINUS | TIMES) expression 
            | NEG expression 
            | RANDOM range? 
            | POINT point 
            | NUMBER 
            | LPAREN expression RPAREN;

point : LPAREN expression COMMA expression RPAREN;
range : LSQUARE expression COMMA expression RSQUARE;