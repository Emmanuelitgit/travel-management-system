import React from "react";
import styles from "@/app/flights/page.module.css";
import Navbar from "../../components/Navbar";
import FlightCard from "@/components/FlightCard";

const page = () => {
  return (
    <div>
      <Navbar />
      <div className={styles.homeBackgroundImage}>
        <div className="flex items-center justify-center mt-20">
          <h1 className="text-white text-5xl font-bold">Fly To Anywhere</h1>
        </div>
        <div className={styles.searchContainer}>
          <div className={styles.categoryContainer}>
            <div>
              <select name="" id="">
                <option value="one way">One Way</option>
                <option value="rounded">Round</option>
                <option value="multi-city">Multi City</option>
              </select>
            </div>

            <div className={styles.tripClass}>
              <select name="tripClass" id="">
                <option value="Economy">Economy</option>
                <option value="premiumEconomy">Premium</option>
                <option value="Business">Business</option>
                <option value="firstClass">First Class</option>
              </select>
            </div>

            <div className="flex gap-1">
              <input type="checkbox" className="w-4" />
              <label htmlFor="">Non-Stop Only</label>
            </div>
          </div>
          <div className={styles.inputContainer}>
            <input type="text" placeholder="Leaving from.." />
            <input type="text" placeholder="Going to.." />
            <input type="date" placeholder="start" />
            <button className="bg-blue-600 p-2 w-3/12 rounded-sm text-white">
              Serach
            </button>
          </div>
        </div>
        <div className={styles.flightsContainer}>
          <div className={styles.filterButtonContainer}>
            <button className={styles.filterButton}>Rounded Trip</button>
            <button className={styles.filterButtonGray}>One Way</button>
          </div>
          <FlightCard />
        </div>
      </div>
    </div>
  );
};

export default page;
