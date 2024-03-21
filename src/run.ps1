function Run {
    param(
        [string]$flag
    )
    javac *.java
    java App $flag
    Remove-Item *.class
    Remove-Item gui/*.class
    Remove-Item gui/backend/*.class
}

if ($args.Length -eq 1) {
    $flag = $args[0]
}

Run $flag