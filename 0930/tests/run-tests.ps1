param(
    [Parameter(Mandatory=$true)][string]$MySqlUrl,
    [string]$MySqlUser = "root"
)
$ErrorActionPreference = "Stop"
$projectDir = Split-Path $PSScriptRoot -Parent
$libs = Join-Path $projectDir "target/test-libs"
$runDir = Join-Path $projectDir ("target/test-" + [guid]::NewGuid().ToString("N"))
$webapp = Join-Path $runDir "webapp"
New-Item -ItemType Directory -Force $webapp | Out-Null
node (Join-Path $PSScriptRoot "download-libs.cjs") $libs
if ($LASTEXITCODE -ne 0) { throw "Test dependency download failed" }
Copy-Item (Join-Path $projectDir "src/main/webapp/*") $webapp -Recurse -Force
$classes = Join-Path $webapp "WEB-INF/classes"
$testClasses = Join-Path $runDir "test-classes"
$sources = (Get-ChildItem (Join-Path $projectDir "src/main/java") -Filter *.java -Recurse).FullName
java -cp "$libs/ecj.jar" org.eclipse.jdt.internal.compiler.batch.Main -17 -encoding UTF-8 -cp "$libs/tomcat-core.jar" -d $classes $sources
if ($LASTEXITCODE -ne 0) { throw "Application compilation failed" }
java -cp "$libs/ecj.jar" org.eclipse.jdt.internal.compiler.batch.Main -17 -encoding UTF-8 -cp "$libs/tomcat-core.jar;$classes" -d $testClasses (Join-Path $PSScriptRoot "IntegrationTest.java")
if ($LASTEXITCODE -ne 0) { throw "Test compilation failed" }
$classpath = "$libs/*;$classes;$webapp/WEB-INF/lib/*;$testClasses"
java "-Dtest.mysql.url=$MySqlUrl" "-Dtest.mysql.user=$MySqlUser" -cp $classpath IntegrationTest $webapp (Join-Path $projectDir "database/schema.sql") (Join-Path $runDir "tomcat")
if ($LASTEXITCODE -ne 0) { throw "Integration test failed" }
java -cp $classpath org.apache.jasper.JspC -uriroot $webapp -d (Join-Path $runDir "jsp") -compile -javaEncoding UTF-8 -die1
if ($LASTEXITCODE -ne 0) { throw "JSP compilation failed" }
