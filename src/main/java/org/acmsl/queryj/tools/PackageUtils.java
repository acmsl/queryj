//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-today  Jose San Leandro Armendariz
                              chous@acm-sl.org

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: jose.sanleandro@acm-sl.com

 ******************************************************************************
 *
 * Filename: PackageUtils.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Provides some useful methods for retrieving package
 *              information about the generated code.
 *
 */
package org.acmsl.queryj.tools;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.patterns.Utils;
import org.acmsl.commons.utils.StringUtils;
import org.acmsl.commons.utils.StringValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.io.File;

/**
 * Provides some useful methods for retrieving package information about
 * the generated code.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class PackageUtils
    implements  Singleton,
                Utils
{
    /**
     * The package prefix for unit tests.
     */
    public static final String UNITTESTS_PACKAGE_PREFIX = "unittests";

    /**
     * The "main" branch if using subfolders.
     */
    public static final String MAIN_BRANCH = "main";

    /**
     * The "test" branch if using subfolders.
     */
    public static final String TEST_BRANCH = "test";

    /**
     * The subpackage for BaseDAO entitites.
     */
    public static final String BASE_DAO_SUBPACKAGE = "dao";

    /**
     * The subpackage for ValueObject entities.
     */
    public static final String VALUE_OBJECT_SUBPACKAGE = "vo";

    /**
     * The subpackage for ValueObject entities.
     */
    public static final String BASE_VALUE_OBJECT_SUBPACKAGE =
        VALUE_OBJECT_SUBPACKAGE;

    /**
     * The subpackage for ValueObject implementations.
     */
    public static final String VALUE_OBJECT_IMPL_SUBPACKAGE =
        VALUE_OBJECT_SUBPACKAGE;

    /**
     * The subpackage for Relational Database common classes.
     */
    public static final String RDB_SUBPACKAGE = "rdb";

    /**
     * The subpackage for TableRepository.
     */
    public static final String TABLE_REPOSITORY_SUBPACKAGE = "tables";

    /**
     * The subpackage for Function entities.
     */
    public static final String FUNCTIONS_SUBPACKAGE = "functions";

    /**
     * The subpackage for MockDAO entities.
     */
    public static final String MOCK_DAO_SUBPACKAGE = "mock";

    /**
     * The subpackage for XmlDAO entities.
     */
    public static final String XML_DAO_SUBPACKAGE = "xml";

    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class PackageUtilsSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final PackageUtils SINGLETON = new PackageUtils();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected PackageUtils() {};

    /**
     * Retrieves a PackageUtils instance.
     * @return such instance.
     */
    @NotNull
    public static PackageUtils getInstance()
    {
        return PackageUtilsSingletonContainer.SINGLETON;
    }

    /**
     * Retrieves the package name for given subpackage.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @return the complate package information for given subpackage.
     * @precondition packageName != null
     */
    protected String retrievePackage(
        final String packageName, final String subpackage)
    {
        return
            retrievePackage(
                packageName,
                subpackage,
                StringValidator.getInstance());
    }

    /**
     * Retrieves the package name for given subpackage.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param stringValidator the <code>StringValidator</code> instance.
     * @return the complate package information for given subpackage.
     * @precondition packageName != null
     * @precondition stringValidator != null
     */
    protected String retrievePackage(
        final String packageName,
        final String subpackage,
        @NotNull final StringValidator stringValidator)
    {
        String result = packageName;

        if  (!stringValidator.isEmpty(subpackage))
        {
            if  (!stringValidator.isEmpty(packageName))
            {
                result += ".";
            }
            else
            {
                result = "";
            }

            result += subpackage;
        }

        return result;
    }

    /**
     * Retrieves the package name for given subpackage.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param subFolders whether to use subfolders or not.
     * @return the complate package information for given subpackage.
     * @precondition packageName != null
     * @precondition subpackage != null
     */
    protected String retrieveTestPackage(
        final String packageName,
        final String subpackage,
        final boolean subFolders)
    {
        return
            retrievePackage(
                retrievePackage(
                    (subFolders) ? "" : UNITTESTS_PACKAGE_PREFIX,
                    packageName),
                subpackage);
    }

    /**
     * Retrieves the package name for given subpackage.
     * @param packageName the original package.
     * @param subFolders whether to use subfolders or not.
     * @return the complate package information for given subpackage.
     * @precondition packageName != null
     */
    protected String retrieveTestPackage(
        final String packageName, final boolean subFolders)
    {
        return retrieveTestPackage(packageName, "", subFolders);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @NotNull
    protected File retrieveFolder(
        @NotNull final File parentFolder, final String packageName)
    {
        return retrieveFolder(parentFolder, packageName, false);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @NotNull
    protected File retrieveFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveFolder(parentFolder, packageName, useSubfolders, false);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param useSubfolders whether to use subfolders or not.
     * @param testPackage whether the package contains unit tests.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @Nullable
    protected File retrieveFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final String subpackage,
        final boolean useSubfolders,
        final boolean testPackage)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                subpackage,
                useSubfolders,
                testPackage,
                StringUtils.getInstance(),
                StringValidator.getInstance());
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param useSubfolders whether to use subfolders or not.
     * @param testPackage whether the package contains unit tests.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @param stringValidator the <code>StringValidator</code> instance.
     * @return the folder in which the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition subpackage != null
     * @precondition stringUtils != null
     * @precondition stringValidator != null
     */
    @Nullable
    protected File retrieveFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final String subpackage,
        final boolean useSubfolders,
        final boolean testPackage,
        @NotNull final StringUtils stringUtils,
        @NotNull final StringValidator stringValidator)
    {
        @Nullable File result = null;

        String t_strWholePackage = packageName;

        if  (!stringValidator.isEmpty(subpackage))
        {
            t_strWholePackage += "." + subpackage;
        }

        result =
            retrieveFolder(
                parentFolder,
                t_strWholePackage,
                useSubfolders,
                testPackage,
                stringUtils);

        return result;
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders or not.
     * @param testPackage whether the package contains unit tests.
     * @return the folder in which the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @NotNull
    protected File retrieveFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders,
        final boolean testPackage)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                useSubfolders,
                testPackage,
                StringUtils.getInstance());
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders or not.
     * @param testPackage whether the package contains unit tests.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return the folder in which the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition stringUtils != null
     */
    @NotNull
    protected File retrieveFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders,
        final boolean testPackage,
        @NotNull final StringUtils stringUtils)
    {
        String t_strResult = parentFolder.getPath();

        if  (useSubfolders)
        {
            t_strResult += File.separator;

            if  (testPackage)
            {
                t_strResult += TEST_BRANCH;
            }
            else
            {
                t_strResult += MAIN_BRANCH;
            }
        }

        t_strResult +=
            File.separator + stringUtils.packageToFilePath(packageName);

        return new File(t_strResult);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param useSubfolders whether to use subfolders or not.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     */
    @NotNull
    protected File retrieveTestFolder(
        @NotNull final File parentFolder, final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                (useSubfolders ? "" : UNITTESTS_PACKAGE_PREFIX),
                useSubfolders,
                true);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param useSunfolders whether to use subfolders.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @NotNull
    protected File retrieveTestFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                useSubfolders,
                true);
    }

    /**
     * Retrieves the folder for given subpackage.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param subpackage the subpackage.
     * @param useSunfolders whether to use subfolders.
     * @return the folder in which the associated class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @Nullable
    protected File retrieveTestFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final String subpackage,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveTestFolder(parentFolder, useSubfolders),
                packageName,
                subpackage,
                useSubfolders,
                true);
    }

    /**
     * Retrieves the package name for base DAO templates.
     * @param packageName the original package.
     * @return the package for the associated base DAO interface.
     * @precondition packageName != null
     */
    public String retrieveBaseDAOPackage(final String packageName)
    {
        return retrievePackage(packageName, BASE_DAO_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for base DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @Nullable
    public File retrieveBaseDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                BASE_DAO_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for base DAO factory templates.
     * @param packageName the original package.
     * @return the package for the associated base DAO factory class.
     * @precondition packageName != null
     */
    public String retrieveBaseDAOFactoryPackage(
        final String packageName)
    {
        return retrieveBaseDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for base DAO factory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO factory should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @Nullable
    public File retrieveBaseDAOFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveBaseDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for value object templates.
     * @param packageName the original package.
     * @return the package for the associated value object class.
     * @precondition packageName != null
     */
    public String retrieveValueObjectPackage(String packageName)
    {
        return retrievePackage(packageName, VALUE_OBJECT_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for value object templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated Jdbc DAO should be
     * generated.
     */
    @Nullable
    public File retrieveValueObjectFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                VALUE_OBJECT_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for value object factory
     * templates.
     * @param packageName the original package.
     * @return the package for the associated value object
     * factory class.
     */
    public String retrieveValueObjectFactoryPackage(final String packageName)
    {
        return retrieveValueObjectPackage(packageName);
    }

    /**
     * Retrieves the folder for value object factory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated value object
     * factories should be generated.
     */
    @Nullable
    public File retrieveValueObjectFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveValueObjectFolder(
                parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for baseValue object templates.
     * @param packageName the original package.
     * @return the package for the associated baseValue object class.
     * @precondition packageName != null
     */
    public String retrieveBaseValueObjectPackage(final String packageName)
    {
        return retrievePackage(packageName, BASE_VALUE_OBJECT_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for baseValue object templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated Jdbc DAO should be
     * generated.
     */
    @Nullable
    public File retrieveBaseValueObjectFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                BASE_VALUE_OBJECT_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for value object implementation templates.
     * @param packageName the original package.
     * @return the package for the associated value object class.
     * @precondition packageName != null
     */
    public String retrieveValueObjectImplPackage(final String packageName)
    {
        return retrievePackage(packageName, VALUE_OBJECT_IMPL_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for value object implementation templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated Jdbc DAO should be
     * generated.
     */
    @Nullable
    public File retrieveValueObjectImplFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                VALUE_OBJECT_IMPL_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for DataAccessManager templates.
     * @param packageName the original package.
     * @return the package for the associated manager class.
     */
    public String retrieveDataAccessManagerPackage(
        final String packageName)
    {
        return retrieveBaseDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for DataAccessManager templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated manager should be
     * generated.
     */
    @Nullable
    public File retrieveDataAccessManagerFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveBaseDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for base Relational Database common classes.
     * @param packageName the original package.
     * @return the package for the associated rdb classes.
     */
    public String retrieveRdbPackage(final String packageName)
    {
        return
            retrievePackage(
                retrieveBaseDAOPackage(packageName), RDB_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for base Relational Database common classes.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated rdb classes should be
     * generated.
     */
    @NotNull
    public File retrieveRdbFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveBaseDAOFolder(
                    parentFolder, packageName, useSubfolders),
                RDB_SUBPACKAGE);
    }

    /**
     * Retrieves the package name for Jdbc DAO templates.
     * @param packageName the original package.
     * @return the package for the associated Jdbc DAO class.
     */
    public String retrieveJdbcDAOPackage(final String packageName)
    {
        return retrieveRdbPackage(packageName);
    }

    /**
     * Retrieves the folder for Jdbc DAO factory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated Jdbc DAO should be
     * generated.
     */
    @NotNull
    public File retrieveJdbcDAOFolder(
        @NotNull final File parentFolder, final String packageName, final boolean useSubfolders)
    {
        return retrieveRdbFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the subpackage name for DAO templates.
     * @param engineName the DAO engine.
     * @return the subpackage for the associated DAO class.
     * @precondition engineName != null
     */
    public String retrieveDAOSubpackage(@NotNull final String engineName)
    {
        return engineName.toLowerCase();
    }

    /**
     * Retrieves the package name for DAO templates.
     * @param packageName the original package.
     * @param engineName the DAO engine.
     * @return the package for the associated DAO class.
     * @precondition packageName != null
     * @precondition engineName != null
     */
    public String retrieveDAOPackage(
        final String packageName, @NotNull final String engineName)
    {
        return
            retrievePackage(
                retrieveRdbPackage(packageName),
                retrieveDAOSubpackage(engineName));
    }

    /**
     * Retrieves the folder for DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param engineName the DAO engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     */
    @NotNull
    public File retrieveDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveRdbFolder(
                    parentFolder, packageName, useSubfolders),
                engineName.toLowerCase());
    }

    /**
     * Retrieves the package name for DAO factory templates.
     * @param packageName the original package.
     * @param engineName the DAO engine.
     * @return the package for the associated DAO factory class.
     */
    public String retrieveDAOFactoryPackage(
        final String packageName, @NotNull final String engineName)
    {
        return retrieveDAOPackage(packageName, engineName);
    }

    /**
     * Retrieves the folder for DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param engineName the DAO engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAO factory
     * should be generated.
     */
    @NotNull
    public File retrieveDAOFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveDAOFolder(
                parentFolder, packageName, engineName, useSubfolders);
    }

    /**
     * Retrieves the package name for repository templates.
     * @param packageName the original package.
     * @return the package for the associated repository class.
     */
    public String retrieveTableRepositoryPackage(
        final String packageName)
    {
        return retrievePackage(packageName, TABLE_REPOSITORY_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for repository templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated should be
     * generated.
     */
    @Nullable
    public File retrieveTableRepositoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                TABLE_REPOSITORY_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for table templates.
     * @param packageName the original package.
     * @return the package for the associated table classes.
     */
    public String retrieveTablePackage(final String packageName)
    {
        return retrieveTableRepositoryPackage(packageName);
    }

    /**
     * Retrieves the folder for table templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated templates should be
     * generated.
     */
    @Nullable
    public File retrieveTableFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveTableRepositoryFolder(
                parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for DAO test templates.
     * @param packageName the original package.
     * @param engineName the engine name.
     * @param subFolders whether to use subfolders or not.
     * @return the package for the associated DAO tests.
     */
    public String retrieveDAOTestPackage(
        final String packageName,
        @NotNull final String engineName,
        final boolean subFolders)
    {
        return
            retrieveTestPackage(
                retrieveDAOPackage(packageName, engineName), subFolders);
    }

    /**
     * Retrieves the folder for DAO test templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine name.
     * @param useSubfolders whether to use subfolders or not.
     * @return the folder in which the associated test class should be
     * generated.
     */
    @NotNull
    public File retrieveDAOTestFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveTestFolder(
                parentFolder,
                retrieveDAOTestPackage(packageName, engineName, useSubfolders),
                useSubfolders);
    }

    /**
     * Retrieves the package name for the base test suite template.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the package for the associated suite.
     */
    public String retrieveBaseTestSuitePackage(
        final String packageName, final boolean useSubfolders)
    {
        return retrieveTestPackage(packageName, useSubfolders);
    }

    /**
     * Retrieves the folder for the base test suite template.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated suite class should be
     * generated.
     */
    @NotNull
    public File retrieveBaseTestSuiteFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveTestFolder(parentFolder, useSubfolders),
                packageName);
    }

    /**
     * Retrieves the package name for the functions templates.
     * @param packageName the original package.
     * @return the package for the associated functions classes.
     */
    public String retrieveFunctionsPackage(final String packageName)
    {
        return
            retrievePackage(packageName, FUNCTIONS_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for the functions templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated functions classes should be
     * generated.
     */
    @Nullable
    public File retrieveFunctionsFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                parentFolder,
                packageName,
                FUNCTIONS_SUBPACKAGE,
                useSubfolders,
                false);
    }

    /**
     * Retrieves the package name for the functions test templates.
     * @param packageName the original package.
     * @param subFolders whether to use subfolders or not.
     * @return the package for the associated test classes.
     */
    public String retrieveTestFunctionsPackage(
        final String packageName, final boolean subFolders)
    {
        return
            retrieveTestPackage(
                retrieveFunctionsPackage(packageName), subFolders);
    }

    /**
     * Retrieves the folder for the functions test templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated functions classes should be
     * generated.
     */
    @NotNull
    public File retrieveTestFunctionsFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveTestFolder(
                parentFolder,
                retrieveTestFunctionsPackage(packageName, useSubfolders),
                useSubfolders);
    }

    /**
     * Retrieves the package name for DAOChooser templates.
     * @param packageName the original package.
     * @return the package for the associated manager class.
     */
    public String retrieveDAOChooserPackage(
        final String packageName)
    {
        return retrieveBaseDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for DAOChooser templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated template should be
     * generated.
     */
    @Nullable
    public File retrieveDAOChooserFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveBaseDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for ProcedureRepository templates.
     * @param packageName the original package.
     * @return the package for the associated repository class.
     */
    public String retrieveProcedureRepositoryPackage(
        final String packageName)
    {
        return retrieveFunctionsPackage(packageName);
    }

    /**
     * Retrieves the folder for ProcedureRepository templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated template should be
     * generated.
     */
    @Nullable
    public File retrieveProcedureRepositoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveFunctionsFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for KeywordRepository templates.
     * @param packageName the original package.
     * @return the package for the associated repository class.
     */
    public String retrieveKeywordRepositoryPackage(
        final String packageName)
    {
        return retrieveProcedureRepositoryPackage(packageName);
    }

    /**
     * Retrieves the folder for KeywordRepository templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated template should be
     * generated.
     */
    @Nullable
    public File retrieveKeywordRepositoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveProcedureRepositoryFolder(
                parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for Mock DAO templates.
     * @param packageName the original package.
     * @return the package for the associated DAO class.
     */
    public String retrieveMockDAOPackage(final String packageName)
    {
        return
            retrievePackage(
                retrieveBaseDAOPackage(packageName),
                MOCK_DAO_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for Mock DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO class should be
     * generated.
     */
    @NotNull
    public File retrieveMockDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveBaseDAOFolder(parentFolder, packageName, useSubfolders),
                MOCK_DAO_SUBPACKAGE);
    }

    /**
     * Retrieves the package name for Mock DAO factory templates.
     * @param packageName the original package.
     * @return the package for the associated DAO factory class.
     */
    public String retrieveMockDAOFactoryPackage(final String packageName)
    {
        return retrieveMockDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for Mock DAO factory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO factory should be
     * generated.
     */
    @NotNull
    public File retrieveMockDAOFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveMockDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for Mock DAO test templates.
     * @param packageName the original package.
     * @param subFolders whether to use subfolders or not.
     * @return the package for the associated Mock DAO tests.
     */
    public String retrieveMockDAOTestPackage(
        final String packageName, final boolean subFolders)
    {
        return
            retrieveTestPackage(
                retrieveMockDAOPackage(packageName), subFolders);
    }

    /**
     * Retrieves the folder for Mock DAO test templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated test class should be
     * generated.
     */
    @NotNull
    public File retrieveMockDAOTestFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveMockDAOFolder(
                retrieveTestFolder(parentFolder, useSubfolders),
                packageName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for XML DAO templates.
     * @param packageName the original package.
     * @return the package for the associated DAO class.
     */
    public String retrieveXMLDAOPackage(final String packageName)
    {
        return
            retrievePackage(
                retrieveBaseDAOPackage(packageName),
                XML_DAO_SUBPACKAGE);
    }

    /**
     * Retrieves the folder for XML DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO class should be
     * generated.
     */
    @NotNull
    public File retrieveXMLDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveFolder(
                retrieveBaseDAOFolder(
                    parentFolder, packageName, useSubfolders),
                XML_DAO_SUBPACKAGE);
    }

    /**
     * Retrieves the package name for XML DAO factory templates.
     * @param packageName the original package.
     * @return the package for the associated DAO factory class.
     */
    public String retrieveXmlDAOFactoryPackage(final String packageName)
    {
        return retrieveXMLDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for XML DAO factory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which  the associated DAO factory should be
     * generated.
     */
    @NotNull
    public File retrieveXmlDAOFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveXMLDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for XML DAO test templates.
     * @param packageName the original package.
     * @param subFolders whether to use subfolders or not.
     * @return the package for the associated XML DAO tests.
     */
    public String retrieveXMLDAOTestPackage(
        final String packageName, final boolean subFolders)
    {
        return
            retrieveTestPackage(
                retrieveXMLDAOPackage(packageName), subFolders);
    }

    /**
     * Retrieves the folder for XML DAO test templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated test class should be
     * generated.
     */
    @NotNull
    public File retrieveXMLDAOTestFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveXMLDAOFolder(
                retrieveTestFolder(parentFolder, useSubfolders),
                packageName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for XMLValueObjectFactory templates.
     * @param packageName the original package.
     * @return the package for the associated factory class.
     */
    public String retrieveXMLValueObjectFactoryPackage(final String packageName)
    {
        return retrieveXMLDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for XMLValueObjectFactory templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated factory class should be
     * generated.
     */
    @NotNull
    public File retrieveXMLValueObjectFactoryFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveXMLDAOFolder(
                parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for the JDBC operation classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @return the package for the associated pointers.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    public String retrieveJdbcOperationsPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName)
    {
        return
            retrieveJdbcOperationsPackage(
                packageName,
                engineName,
                tableName,
                StringUtils.getInstance());
    }

    /**
     * Retrieves the package name for the JDBC operation classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return the package for the associated pointers.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     * @precondition stringUtils != null
     */
    public String retrieveJdbcOperationsPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        @NotNull final StringUtils stringUtils)
    {
        return
            retrievePackage(
                retrieveDAOPackage(
                    packageName, engineName),
                stringUtils.capitalize(
                    tableName.toLowerCase(),
                    '_').toLowerCase());
    }

    /**
     * Retrieves the folder for the JDBC operation classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    @NotNull
    public File retrieveJdbcOperationsFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders)
    {
        return
            retrieveJdbcOperationsFolder(
                parentFolder,
                packageName,
                engineName,
                tableName,
                useSubfolders,
                StringUtils.getInstance());
    }

    /**
     * Retrieves the folder for the JDBC operation classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     * @precondition stringUtils != null
     */
    @NotNull
    public File retrieveJdbcOperationsFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders,
        @NotNull final StringUtils stringUtils)
    {
        return
            retrieveFolder(
                retrieveDAOFolder(
                    parentFolder, packageName, engineName, useSubfolders),
                stringUtils.capitalize(
                    tableName.toLowerCase(),
                    '_').toLowerCase());
    }

    /**
     * Retrieves the package name for QueryPreparedStatementCreator class.
     * @param packageName the original package.
     * @return the package for such class.
     */
    public String retrieveQueryPreparedStatementCreatorPackage(
        final String packageName)
    {
        return retrieveRdbPackage(packageName);
    }

    /**
     * Retrieves the folder for QueryPreparedStatementCreator class.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated rdb classes should be
     * generated.
     */
    @NotNull
    public File retrieveQueryPreparedStatementCreatorFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveRdbFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for the ResultSetExtractor classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @return the package for the associated classes.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    public String retrieveResultSetExtractorPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName)
    {
        return
            retrieveJdbcOperationsPackage(
                packageName, engineName, tableName);
    }

    /**
     * Retrieves the folder for the ResultSetExtractor classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    @NotNull
    public File retrieveResultSetExtractorFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders)
    {
        return
            retrieveJdbcOperationsFolder(
                parentFolder,
                packageName,
                engineName,
                tableName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for the ResultSetExtractor classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @return the package for the associated classes.
     * @precondition packageName != null
     * @precondition engineName != null
     */
    public String retrieveCustomResultSetExtractorPackage(
        final String packageName, @NotNull final String engineName)
    {
        return retrieveDAOPackage(packageName, engineName);
    }

    /**
     * Retrieves the folder for the ResultSetExtractor classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     */
    @NotNull
    public File retrieveCustomResultSetExtractorFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveDAOFolder(
                parentFolder, packageName, engineName, useSubfolders);
    }

    /**
     * Retrieves the package name for the AttributesStatementSetter classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @return the package for the associated classes.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    public String retrieveAttributesStatementSetterPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName)
    {
        return
            retrieveJdbcOperationsPackage(
                packageName, engineName, tableName);
    }

    /**
     * Retrieves the folder for the AttributesStatementSetter classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    @NotNull
    public File retrieveAttributesStatementSetterFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders)
    {
        return
            retrieveJdbcOperationsFolder(
                parentFolder,
                packageName,
                engineName,
                tableName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for the PkStatementSetter classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @return the package for the associated classes.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    public String retrievePkStatementSetterPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName)
    {
        return
            retrieveJdbcOperationsPackage(
                packageName, engineName, tableName);
    }

    /**
     * Retrieves the folder for the PkStatementSetter classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    @NotNull
    public File retrievePkStatementSetterFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders)
    {
        return
            retrieveJdbcOperationsFolder(
                parentFolder,
                packageName,
                engineName,
                tableName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for the FkStatementSetter classes.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @return the package for the associated classes.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    public String retrieveFkStatementSetterPackage(
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName)
    {
        return
            retrieveJdbcOperationsPackage(
                packageName, engineName, tableName);
    }

    /**
     * Retrieves the folder for the FkStatementSetter classes.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param engineName the engine.
     * @param tableName the table name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder for the associated pointers.
     * @precondition parentFolder != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition tableName != null
     */
    @NotNull
    public File retrieveFkStatementSetterFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        @NotNull final String tableName,
        final boolean useSubfolders)
    {
        return
            retrieveJdbcOperationsFolder(
                parentFolder,
                packageName,
                engineName,
                tableName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for RepositoryDAO templates.
     * @param packageName the original package.
     * @param engineName the engine.
     * @return the package for the associated DAO class.
     */
    public String retrieveRepositoryDAOPackage(
        final String packageName, @NotNull final String engineName)
    {
        return retrieveDAOPackage(packageName, engineName);
    }

    /**
     * Retrieves the folder for RepositoryDAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param engineName the engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAO should be
     * generated.
     */
    @NotNull
    public File retrieveRepositoryDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveDAOFolder(
                parentFolder, packageName, engineName, useSubfolders);
    }

    /**
     * Retrieves the package name for BaseRepositoryDAO templates.
     * @param packageName the original package.
     * @return the package for the associated DAO class.
     */
    public String retrieveBaseRepositoryDAOPackage(
        final String packageName)
    {
        return retrieveBaseDAOPackage(packageName);
    }

    /**
     * Retrieves the folder for BaseRepositoryDAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAO should be
     * generated.
     */
    @Nullable
    public File retrieveBaseRepositoryDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveBaseDAOFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for base abstract DAO templates.
     * @param packageName the original package.
     * @return the package for the associated base DAO interface.
     * @precondition packageName != null
     */
    public String retrieveBaseAbstractDAOPackage(final String packageName)
    {
        return retrieveRdbPackage(packageName);
    }

    /**
     * Retrieves the folder for base abstract DAO templates.
     * @param parentFolder the parent folder.
     * @param packageName the original package.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAO class should be
     * generated.
     * @precondition parentFolder != null
     * @precondition packageName != null
     */
    @NotNull
    public File retrieveBaseAbstractDAOFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return
            retrieveRdbFolder(
                parentFolder,
                packageName,
                useSubfolders);
    }

    /**
     * Retrieves the package name for DAOListener templates.
     * @param packageName the original package.
     * @param engineName the engine.
     * @return the package for the associated DAOListener class.
     */
    public String retrieveDAOListenerPackage(
        final String packageName, @NotNull final String engineName)
    {
        return retrieveDAOPackage(packageName, engineName);
    }

    /**
     * Retrieves the folder for DAOListener templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param engineName the engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAOListener should be
     * generated.
     */
    @NotNull
    public File retrieveDAOListenerFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveDAOFolder(
                parentFolder, packageName, engineName, useSubfolders);
    }

    /**
     * Retrieves the package name for DAOListenerImpl templates.
     * @param packageName the original package.
     * @param engineName the engine.
     * @return the package for the associated DAOListenerImpl class.
     */
    public String retrieveDAOListenerImplPackage(
        final String packageName, @NotNull final String engineName)
    {
        return retrieveDAOListenerPackage(packageName, engineName);
    }

    /**
     * Retrieves the folder for DAOListenerImpl templates.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param engineName the engine.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated DAOListenerImpl should be
     * generated.
     */
    @NotNull
    public File retrieveDAOListenerImplFolder(
        @NotNull final File parentFolder,
        final String packageName,
        @NotNull final String engineName,
        final boolean useSubfolders)
    {
        return
            retrieveDAOListenerFolder(
                parentFolder, packageName, engineName, useSubfolders);
    }

    /**
     * Retrieves the package name for StatisticsProvider class.
     * @param packageName the original package.
     * @return the package for such class.
     */
    public String retrieveStatisticsProviderPackage(
        final String packageName)
    {
        return retrieveRdbPackage(packageName);
    }

    /**
     * Retrieves the folder for StatisticsProvider class.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated rdb classes should be
     * generated.
     */
    @NotNull
    public File retrieveStatisticsProviderFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveRdbFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Retrieves the package name for BaseResultSetExtractor class.
     * @param packageName the original package.
     * @return the package for such class.
     */
    public String retrieveBaseResultSetExtractorPackage(
        final String packageName)
    {
        return retrieveRdbPackage(packageName);
    }

    /**
     * Retrieves the folder for BaseResultSetExtractor class.
     * @param parentFolder the parent folder.
     * @param packageName the package name.
     * @param useSubfolders whether to use subfolders.
     * @return the folder in which the associated rdb classes should be
     * generated.
     */
    @NotNull
    public File retrieveBaseResultSetExtractorFolder(
        @NotNull final File parentFolder,
        final String packageName,
        final boolean useSubfolders)
    {
        return retrieveRdbFolder(parentFolder, packageName, useSubfolders);
    }

    /**
     * Extracts the class name of given fully-qualified class.
     * @param fqcn such information.
     * @return the class name.
     * @precondition fqcn != null
     */
    @Nullable
    public String extractClassName(final String fqdn)
    {
        @Nullable String result = null;

        String[] t_astrPieces = split(fqdn, ".");

        int t_iCount = (t_astrPieces != null) ? t_astrPieces.length : 0;

        if  (t_iCount > 0)
        {
            result = t_astrPieces[t_iCount - 1];
        }

        return result;
    }

    /**
     * Splits given value into chunks separated by a separator.
     * @param value the value.
     * @param separator the separator.
     * @return the chunks.
     * @precondition value != null
     * @precondition separator != null
     */
    public String[] split(final String value, final String separator)
    {
        return split(value, separator, StringUtils.getInstance());
    }

    /**
     * Splits given value into chunks separated by a separator.
     * @param value the value.
     * @param separator the separator.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @return the chunks.
     * @precondition value != null
     * @precondition separator != null
     * @precondition stringUtils != null
     */
    protected String[] split(
        final String value,
        final String separator,
        @NotNull final StringUtils stringUtils)
    {
        return stringUtils.split(value, new String[] { separator });
    }
}
