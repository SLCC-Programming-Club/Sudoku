function Run {
    param(
        [string]$flag
    )
    javac -d bin *.java
    java -cp bin App $flag
}

if ($args.Length -eq 1) {
    $flag = $args[0]
}

Run $flag
