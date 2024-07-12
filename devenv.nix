{ pkgs, ... }: {
  # https://devenv.sh/reference/options/
  packages = [ pkgs.figlet pkgs.lolcat pkgs.babashka pkgs.cljfmt];
  languages.clojure.enable = true;

  enterShell = ''
    figlet -f small -k "Devenv!" | lolcat -F 0.5 -ad 1 -s 30
    export PATH=$PWD/bin:$PATH
  '';
  processes = {
    launchpad.exec = "bin/launchpad";
    kaocha.exec = "bin/kaocha --watch";
  };

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
