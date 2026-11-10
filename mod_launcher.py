import urllib.request
import tarfile
import os
import platform
import shutil
import subprocess
import ssl
import glob
from pathlib import Path
import sys

try:
    import certifi
except ImportError:
    print("Installing missing package: certifi")
    subprocess.check_call([
        sys.executable, "-m", "pip",
        "install", "--user", "certifi"
    ])
    
    print("Restarting launcher...")

    os.execv(
        sys.executable,
        [sys.executable] + sys.argv
    )

print("All Python dependencies are ready!")

# search for HellenicEchoes-master
search_path = Path('.') 
for folder in search_path.rglob('HellenicEchoes-master'):
    if folder.is_dir():
        print(f"Found folder: {folder.resolve()}")
        mod_path = folder.resolve()

install_dir = os.path.expanduser("~/java17")

if os.path.exists(install_dir):
    shutil.rmtree(install_dir)

os.makedirs(install_dir, exist_ok=True)

# Installation directory (no admin required)
install_dir = os.path.expanduser("~/java17")
os.makedirs(install_dir, exist_ok=True)

# Detect Mac architecture
arch = platform.machine()

if arch == "arm64":
    architecture = "aarch64"
else:
    architecture = "x64"

url = (
    f"https://api.adoptium.net/v3/binary/latest/17/ga/mac/"
    f"{architecture}/jdk/hotspot/normal/eclipse"
)

archive = os.path.join(install_dir, "jdk17.tar.gz")

print("Downloading Java 17 JDK...")
context = ssl.create_default_context(cafile=certifi.where())

request = urllib.request.Request(
    url,
    headers={
        "User-Agent": "Mozilla/5.0"
    }
)

with urllib.request.urlopen(request, context=context) as response:
    with open(archive, "wb") as f:
        while True:
            chunk = response.read(1024 * 1024)
            if not chunk:
                break
            f.write(chunk)

print("Extracting...")
with tarfile.open(archive, "r:gz") as tar:
    tar.extractall(install_dir)

os.remove(archive)

print("Java 17 installed!")
print("Location:", install_dir)

print("making the user use java")

install_dir = os.path.expanduser("~/java17")

matches = glob.glob(
    os.path.join(install_dir, "jdk-*/Contents/Home")
)

if not matches:
    raise Exception("Java 17 installation not found!")

java_home = matches[0]

# Configure future Terminal sessions
zshrc = os.path.expanduser("~/.zshrc")

with open(zshrc, "r") as f:
    existing = f.read()

config = f'''
# Minecraft Java 17 configuration
export JAVA_HOME="{java_home}"
export PATH="$JAVA_HOME/bin:$PATH"
'''

if "Minecraft Java 17 configuration" not in existing:
    with open(zshrc, "a") as f:
        f.write(config)

# Configure the current Python process and its child processes
os.environ["JAVA_HOME"] = java_home
os.environ["PATH"] = java_home + "/bin:" + os.environ["PATH"]

# Verify
subprocess.run(["java", "-version"], check=True)

print("Java 17 successfully configured!")

try:
    command = f"""cd {mod_path}
    ./gradlew runClient"""
except NameError:
    print("could not find mod path... assuming launcher iss in mod folder!")
    command = "./gradlew runClient"

subprocess.run(command, shell=True, executable="/bin/bash")