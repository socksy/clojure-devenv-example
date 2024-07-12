# Clojure Devenv example

I believe that the combination of [Lambda Island's Launchpad](https://github.com/lambdaisland/launchpad), [devenv.sh](https://devenv.sh), and Kaocha is a really sweet starting position for dev tooling in the Clojure land. However, I've not really seen anyone give any examples of it, so here's an example setup of how it might look for a basic hello world.

## Installing nix
Nix is a package manager that shines in creating reproducible builds. In this case, it can act a bit like Python's virtual environments but for everything you can ever want. The amount of packages that nix has supported is immense, and with nix flakes it's possible to build an environnment that you're certain will build the same for every person that will try to build it.

To install nix with good defaults installed, I recommend using the [Determinate Systems Nix Installer](https://zero-to-nix.com/concepts/nix-installer), which you can do with:

```bash
curl --proto '=https' --tlsv1.2 -sSf -L https://install.determinate.systems/nix | sh -s -- install
```

## Devenv.sh
The problem with nix, is that while it's quite powerful, it can also be a pain in the arse. You have to learn a new language, which whilst relatively small, is the definition of accidental complexity. Sometimes things are a pain to work out why they're not working, and setting up all the packages and environment variables for systems that don't understand the pure/immutable setup of a nix environment can mean some quite ugly nix files.

This is where devenv.sh comes in - it provides a way to use very simple syntax to just create what you want.

So this example includes both a `flake.nix` file and a `devenv.nix` file, and apart from maybe changing the name inside of `flake.nix`, you can basically ignore that and go straight for `devenv.nix`.

# how to run

First do:
```
nix develop
```

followed by:
```
devenv up
```

# Walkthrough of devenv
Going through that `devenv.nix` file:
```nix
{ pkgs, ... }: {
  # https://devenv.sh/reference/options/
  packages = [ pkgs.figlet pkgs.lolcat pkgs.babashka pkgs.cljfmt];
```
You can add here any package that can be found in the nixpkgs repo. Unless you want something truly obscure, the chances are you can find it [here](https://search.nixos.org/packages). I've included figlet and lolcat for funzies, and babashka and cljfmt as useful tools (babashka in particular is needed for launchpad).

```nix
  languages.clojure.enable = true;
```
You can enable entire ecosystems by just calling `languages.mylanguage.enable = true;`, which both makes sure the correct packages are installed, and that they are set up correctly. [Check out the documentation on devenv.sh](https://devenv.sh/languages/).

```nix
  enterShell = ''
    figlet -f small -k "Devenv!" | lolcat -F 0.5 -ad 1 -s 30
    export PATH=$PWD/bin:$PATH
  '';
```
when you start the developer environment using either `nix develop` or by setting up [nix-direnv](https://github.com/nix-community/nix-direnv) to start it every time you `cd` into the project, then these commands will be run. 

In this case, I've added a nice little welcome message, and made sure that the scripts in `bin/` are executable by adding them to the path. When you leave the developer environment, this will be undone, of course.

```nix
  processes = {
    launchpad.exec = "bin/launchpad";
    kaocha.exec = "bin/kaocha --watch";
  };
```
Running processes can be a really nice way to make sure that everyone on a project is able to instantly have the same developer setup running. When you call `devenv up` whilst in the developer environment, you'll launch a TUI in that terminal window which runs all the processes. 

In this case we're starting the kaocha tests with `--watch`, so they rerun every time you update them, and run launchpad which starts an nREPL that you can connect to from your editor, and also watches for changes to deps.edn to e.g. download a new library on the fly.

```nix
  #services.postgres = {
  #  enable = true;
  #  initialDatabases = [{ name = "foos"; }];
  #  listen_addresses = "127.0.0.1";
  #  port = 5432;
  #  initialScript = ''
  #    CREATE USER foo WITH SUPERUSER PASSWORD 'bar';
  #    '';
  #};
  #env.PGPASSWORD = "bar";

}
```
here I gave an example of how you could run a postgres database when doing `devenv up`. Of course, this is commented out in the example, but there are many cases when you want to have a developer database. In those cases, you are usually left with either leaving instructions to developers on how to set up their own local postgres, or provide a docker-compose file with the ports set up, which can be overkill (especially on macOS where docker requires you to run the environment in a linux virtual machine).


# Using direnv
One advantage of devenvs in nix is that you can set up [direnv](https://direnv.net/) to automatically start the nix environment when you cd into it. This is possible using a nix-direnv, which you can get by installing it with, for example

```bash
nix profile install 'nixpkgs#nix-direnv'
```

After which, you can make an .envrc file. An example one is provided with this project, so

```bash
cp .envrc.sample .envrc && direnv allow
```

The direnv does not start the processes, but is the equivalent of running `nix develop`, meaning you can access babashka, clojure etc, and can also run `devenv up`.
