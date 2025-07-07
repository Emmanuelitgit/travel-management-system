import React from "react";
import styles from "./page.module.css";
import { Flight } from "@mui/icons-material";
import {
  BusAlert,
  Hotel,
  TripOrigin,
  Money,
  LocationCity,
  CarRental,
  Book,
  Logout,
} from "@mui/icons-material";

const Sidebar = () => {
  return (
    <div className={styles.sidebarContainer}>
      <div className="flex flex-row gap-3 ">
        <div className="flex flex-col gap-8 mt-10">
          <div className="flex gap-3 ">
            <Flight />
            <a className="hover:bg-gray-400" href="">
              Flights
            </a>
          </div>
          <div className="flex gap-3">
            <CarRental />
            <a href="">Buses</a>
          </div>
          <div className="flex gap-3">
            <Hotel />
            <a href="">Hotels</a>
          </div>
          <div className="flex gap-3">
            <LocationCity />
            <a href="">Destinations</a>
          </div>
          <div className="flex gap-3">
            <Money />
            <a href="">Fares</a>
          </div>
          <div className="flex gap-3">
            <Book />
            <a href="">My Bookings</a>
          </div>
          <div className="flex gap-3 mt-24">
            <Logout />
            <a href="">Logout</a>
          </div>
        </div>
        <div className={styles.border}></div>
      </div>
    </div>
  );
};

export default Sidebar;
