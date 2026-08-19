{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
  };

  outputs = {nixpkgs, ...}: let
    forAllSystems = function:
      nixpkgs.lib.genAttrs [
        "x86_64-linux"
      ] (system: function nixpkgs.legacyPackages.${system});
  in {
    devShells = forAllSystems (pkgs: {
      default = let
        java = pkgs.jdk21_headless;
        lib = pkgs.lib;
      in
        pkgs.mkShell rec {
          nativeBuildInputs = with pkgs; [
            java
            git
          ];

          buildInputs = with pkgs; [
            libGL
            glfw3-minecraft
            wayland
            libxkbcommon

            libpulseaudio
            openal

            jdt-language-server
            google-java-format
            jetbrains.idea
          ];

          env = {
            LD_LIBRARY_PATH = lib.makeLibraryPath buildInputs;
            JAVA_HOME = "${java.home}";
          };
        };
    });
  };
}
