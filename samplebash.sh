#!/bin/bash
fileName="$1.xml"
externalDirName="$2"
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
echo "Script directory: $SCRIPT_DIR"
echo "going inside apic2apigee"
if [ -z "$2" ]; then
  # The variable is empty or unset.
  cd "${SCRIPT_DIR}/apic2apigee"
else
  # The variable is not empty or unset.
  cd "$externalDirName"
fi
pwd
echo "filename: ${fileName}"
java -cp ../SaxonHE/saxon-he-12.3.jar net.sf.saxon.Query -t -qs:"current-date()"

file="files/${fileName}"

if test -f "$file"; then
  echo "file exist. printing current directory and command to execute"
  pwd
  echo "java -cp ../SaxonHE/saxon-he-12.3.jar net.sf.saxon.Transform -t -s:files/${fileName} -xsl:apic2apigee/apic2apigee.Main.xsl"
  java -cp ../SaxonHE/saxon-he-12.3.jar net.sf.saxon.Transform -t -s:files/${fileName} -xsl:apic2apigee/apic2apigee.Main.xsl
  echo "Transformation Done.."
  echo "Renaming Folder"
  mv "_res_" "$1"
  echo "Folder Renames Successfully"
else
  echo "file does not exist"
fi







