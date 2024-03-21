function Run {
    param(
        [string]$flag
    )
    javac *.java
    java App $flag
    rm *.class
    rm gui/*.class
    rm gui/backend/*.class
}

if ($args.Length -eq 1) {
    $flag = $args[0]
}

Run $flag