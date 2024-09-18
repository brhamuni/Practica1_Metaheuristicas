//
// Created by on 24/10/2023.
//

#ifndef PRACTICA2_CARGAFICHEROS_H
#define PRACTICA2_CARGAFICHEROS_H


#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <cmath>

using namespace std;

class cargaFicheros {
private:
    string fichero;
    vector<vector<double>> ciudades;
    vector<vector<double>> distancias;

public:
    cargaFicheros(string ruta) : fichero(ruta.substr(0, ruta.find('.'))) {
        ifstream archivo(ruta);
        string linea;

        while (getline(archivo, linea)) {
            if (linea.find("DIMENSION") != string::npos) {
                int tam = stoi(linea.substr(linea.find(":") + 1));
                ciudades.resize(tam, vector<double>(2));

//                getline(archivo, linea);
//                getline(archivo, linea);
            }
            if(linea.find("NODE_COORD_SECTION") != string::npos) {
                getline(archivo,linea);
                while (linea.find("EOF") == string::npos) {
                    int indice, i = 0;
                    double x, y;
                    istringstream ss(linea);
                    ss >> indice >> x >> y;

                    ciudades[indice - 1][i++] = x;
                    ciudades[indice - 1][i] = y;

                    getline(archivo, linea);
                }
            }
        }

        distancias.resize(ciudades.size(), vector<double>(ciudades.size()));
        for (int i = 0; i < ciudades.size(); ++i) {
            for (int j = i; j < ciudades.size(); ++j) {
                if (i == j) {
                    distancias[i][j] = numeric_limits<double>::infinity();
                } else {
                    distancias[i][j] = distancias[j][i] = sqrt(pow(ciudades[i][0] - ciudades[j][0], 2) + pow(ciudades[i][1] - ciudades[j][1], 2));
                }
            }
        }
    }

    string getRuta() {
        return fichero;
    }

    vector<vector<double>> getCiudades() {
        return ciudades;
    }

    vector<vector<double>> getDistancias() {
        return distancias;
    }
};















#endif //PRACTICA2_CARGAFICHEROS_H
