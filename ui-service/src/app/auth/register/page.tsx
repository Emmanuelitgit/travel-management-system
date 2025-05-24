"use client";
import React, { useState } from "react";
import styles from "@/app/auth/register/page.module.css";
import PermIdentityIcon from "@mui/icons-material/PermIdentity";
import { useRouter } from "next/navigation";

const page = () => {
  return (
    <div className={styles.container}>
      <div className={styles.formContainer}>
        <div className={styles.titleContainer}>
          <h1 className={styles.titleText}>Sign Up</h1>
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputEmailIcon} />
          <input type="email" placeholder="First Name" name="firstName" />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputEmailIcon} />
          <input type="text" placeholder="Last Name" name="lastName" />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputEmailIcon} />
          <input type="email" placeholder="Email" name="email" />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputPasswordIcon} />
          <input type="password" placeholder="Password" name="password" />
        </div>
        <div className={styles.inputBtnContainer}>
          <button className={styles.inputBtn}>Login</button>
        </div>
        <div className={styles.alreadyHaveAccountContainer}>
          <p className={styles.alreadyHaveAccount}>already have account?</p>
          <span className="text-blue-500 cursor-pointer">sign in</span>
        </div>
      </div>
      <div className={styles.imageContainer}>
        <img
          src="https://bsmedia.business-standard.com/_media/bs/img/article/2024-08/07/full/1722995892-9811.jpg?im=FeatureCrop,size=(826,465)"
          alt=""
        />
      </div>
    </div>
  );
};

export default page;
