function Run {
    param(
        [string]$flag
    )
    javac *.java
    java App $flag
    rm *.class
}

if ($args.Length -eq 1) {
    $flag = $args[0]
}

Run $flag