package no.uio.aeroscript;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.runtime.Interpreter;

// Funker med noen expressions når man kjører testfil: "java -jar build/libs/aeroscript-1.0.jar src/main/resources/expressions.aero"

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Give just one filename.");
        }
        else {
            String path = args[0];

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    AeroScriptLexer lexer = new AeroScriptLexer(CharStreams.fromString(line));
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    AeroScriptParser parser = new AeroScriptParser(tokens);
                    AeroScriptParser.ProgramContext programContext = parser.program();
                    Interpreter interpreter = new Interpreter();
                    Node node = (Node) interpreter.visit(programContext.expression(0));  
                    String result = node.evaluate().toString();
                    System.out.println(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}