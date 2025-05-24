import React from "react";
import styles from "./page.module.css";
import { Search } from "@mui/icons-material";
const Navbar = () => {
  return (
    <div className={styles.container}>
      <div className="flex justify-between items-center w-full ">
        {/* search and title items start here*/}
        <div className="flex flex-row items-center gap-7 w-2/4">
          <h1 className="text-blue-600 text-3xl">Flyway.com</h1>
          <div className="flex w-2/4 items-center">
            <input
              type="text"
              className={styles.searchInput}
              placeholder="search..."
            />
            <Search fontSize="medium" className={styles.searchIcon} />
          </div>
        </div>
        {/* navs start here*/}
        <div className="flex flex-row gap-7">
          <a href="" className={styles.anchor}>
            Home
          </a>
          <a href="" className={styles.anchor}>
            About
          </a>
          <a href="" className={styles.anchor}>
            Customer Support
          </a>
          <a href="" className={styles.anchor}>
            Contact
          </a>
          <button className="bg-blue-500 p-1 outline-none text-white rounded-sm">
            <a href="/auth/login">sign in/Register</a>
          </button>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
