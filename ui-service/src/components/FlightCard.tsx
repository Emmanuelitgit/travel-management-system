import React from "react";
import styles from "@/app/flights/page.module.css";

const FlightCard = () => {
  return (
    <div className={styles.flightsCardContainer}>
      <div className={styles.flightsCard}>
        <div className={styles.flightItemsMainContainer}>
          <div className={styles.flightItemsContainer}>
            <div className={styles.departureDestinationContainer}>
              <div className={styles.departure}>
                <img
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Ghana.svg/1200px-Flag_of_Ghana.svg.png"
                  alt=""
                  className="w-10 rounded"
                />
                <div>
                  <h1>Accra</h1>
                  <span>06:30</span>
                </div>
              </div>
              <div className={styles.timeItem}>
                <span>1h</span>
                <span className={styles.tripTimeBorder}></span>
                <span>Nonstop</span>
              </div>
              <div className={styles.destination}>
                <h1>Tamale</h1>
                <span>06:30</span>
              </div>
            </div>
            <div className={styles.tripItems}>
              <p>Accra-Tamale</p>
              <p>Friday, May 30</p>
              <p>African world lines</p>
            </div>
          </div>
          <div className={styles.priceContainer}>
            <span>GHc200</span>
            <button>Search</button>
          </div>
        </div>
        <div className={styles.flightItemsMainContainer}>
          <div className={styles.flightItemsContainer}>
            <div className={styles.departureDestinationContainer}>
              <div className={styles.departure}>
                <img
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Ghana.svg/1200px-Flag_of_Ghana.svg.png"
                  alt=""
                  className="w-10 rounded"
                />
                <div>
                  <h1>Accra</h1>
                  <span>06:30</span>
                </div>
              </div>
              <div className={styles.timeItem}>
                <span>1h</span>
                <span className={styles.tripTimeBorder}></span>
                <span>Nonstop</span>
              </div>
              <div className={styles.destination}>
                <h1>Tamale</h1>
                <span>06:30</span>
              </div>
            </div>
            <div className={styles.tripItems}>
              <p>Accra-Tamale</p>
              <p>Friday, May 30</p>
              <p>African world lines</p>
            </div>
          </div>
          <div className={styles.priceContainer}>
            <span>GHc200</span>
            <button>Search</button>
          </div>
        </div>
      </div>

      <div className={styles.flightsCard}>
        <div className={styles.flightItemsMainContainer}>
          <div className={styles.flightItemsContainer}>
            <div className={styles.departureDestinationContainer}>
              <div className={styles.departure}>
                <img
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Ghana.svg/1200px-Flag_of_Ghana.svg.png"
                  alt=""
                  className="w-10 rounded"
                />
                <div>
                  <h1>Accra</h1>
                  <span>06:30</span>
                </div>
              </div>
              <div className={styles.timeItem}>
                <span>1h</span>
                <span className={styles.tripTimeBorder}></span>
                <span>Nonstop</span>
              </div>
              <div className={styles.destination}>
                <h1>Tamale</h1>
                <span>06:30</span>
              </div>
            </div>
            <div className={styles.tripItems}>
              <p>Accra-Tamale</p>
              <p>Friday, May 30</p>
              <p>African world lines</p>
            </div>
          </div>
          <div className={styles.priceContainer}>
            <span>GHc200</span>
            <button>Search</button>
          </div>
        </div>
        <div className={styles.flightItemsMainContainer}>
          <div className={styles.flightItemsContainer}>
            <div className={styles.departureDestinationContainer}>
              <div className={styles.departure}>
                <img
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Ghana.svg/1200px-Flag_of_Ghana.svg.png"
                  alt=""
                  className="w-10 rounded"
                />
                <div>
                  <h1>Accra</h1>
                  <span>06:30</span>
                </div>
              </div>
              <div className={styles.timeItem}>
                <span>1h</span>
                <span className={styles.tripTimeBorder}></span>
                <span>Nonstop</span>
              </div>
              <div className={styles.destination}>
                <h1>Tamale</h1>
                <span>06:30</span>
              </div>
            </div>
            <div className={styles.tripItems}>
              <p>Accra-Tamale</p>
              <p>Friday, May 30</p>
              <p>African world lines</p>
            </div>
          </div>
          <div className={styles.priceContainer}>
            <span>GHc200</span>
            <button>Search</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FlightCard;
